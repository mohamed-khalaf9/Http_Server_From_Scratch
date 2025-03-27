import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;

public class ClientHandler implements  Runnable{
    private Socket clientSocket;
    private Router router;
    private String clientIpAddresse;
    private static final Set<String> METHODS_WITH_BODY = Set.of("POST", "PUT", "PATCH");
    private long requestArrivalTime;


    ClientHandler(Socket socket,Router router,String clientIpAddresse)
    {
        this.clientSocket = socket;
        this.router = router;
        this.clientIpAddresse = clientIpAddresse;


    }


    public void run() {
        BufferedReader in = null;
        BufferedWriter out = null;
        boolean keepAlive = true;
        RateLimiter rateLimiter = HttpServer.IP_RATE_LIMITER_MAP.get(clientIpAddresse);
        Logger logger = new Logger();

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            while (keepAlive) {
                clientSocket.setSoTimeout(60000); // timout for client connection

                HttpRequest request = parseRequest(in);
                HttpRequestLog requestLog = new HttpRequestLog(request, this.clientIpAddresse, this.requestArrivalTime);
                HttpResponse response;

                if (request == null) {
                    response = new HttpResponse();
                    response.setStatusCode(400);
                    response.setStatusText("Bad Request");
                    response.setBody("No request found");
                    keepAlive = false;
                }
                else if (!rateLimiter.allowRequest(requestArrivalTime)) {
                    response = new HttpResponse();
                    response.setStatusCode(429);
                    response.setStatusText("Too Many Requests");
                    response.setBody("Too Many Requests");
                    keepAlive = false;
                }
                else {
                    System.out.println("Received Request: " + request.toString());

                    Function<HttpRequest, HttpResponse> handler = router.getHandler(request.getMethod(), request.getTarget());
                    if (handler != null) {
                        response = handler.apply(request);
                    } else {
                        response = new HttpResponse();
                        response.setStatusCode(404);
                        response.setStatusText("Handler Not Found");
                    }
                }


                Utilities.sendResponse(out, response);

                // Log both request and response together
                long currentTime = System.currentTimeMillis();
                long processingTime = currentTime - requestArrivalTime;
                HttpResponseLog responseLog = new HttpResponseLog(requestLog.getRequestId(), this.clientIpAddresse, processingTime, response);
                logger.logRequestAndResponse(requestLog, responseLog);

                // update keep-alive value
                HeadersDetector detector = new HeadersDetector();
                keepAlive = detector.isPersistantConnection(request.getHeaders());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    HttpResponse response = new HttpResponse();
                    response.setStatusCode(500);
                    response.setStatusText("Connection Closed");
                    Utilities.sendResponse(out, response);
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




    public HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException, InterruptedException {
        String requestLine = bufferedReader.readLine();
        if(requestLine == null)
            return null;

        // log actual arrival time of the request
        this.requestArrivalTime = System.currentTimeMillis();

        // fetch method, target and version
        String[] requestLineParts = requestLine.split(" ", 3);
        if (requestLineParts.length < 3) {
            throw new IOException("Invalid request line");
        }
        String method = requestLineParts[0];
        String target = requestLineParts[1];
        String[] targetParts = target.split("/");
        String requestTarget = "/"+targetParts[1]; // example: /create/fileName so requestTarget = /create
        String pathParameter = targetParts[2];// pathParameter = fileName

        // there is an empty line between headers and body so keep parsing untill reach this empty line
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = bufferedReader.readLine()) != null && !headerLine.isEmpty()) {
            // parse the corrected written headers
            int colonIdx = headerLine.indexOf(':');
            if (colonIdx != -1) {
                String key = headerLine.substring(0, colonIdx).trim();
                String value = headerLine.substring(colonIdx + 1).trim();
                headers.put(key, value);
            }
        }

        // parse the body using content length header value
        String body = null;
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length").trim());
            char[] bodyChars = new char[contentLength];
            int totalRead = 0;
            while (totalRead < contentLength) {
                int readBytes = bufferedReader.read(bodyChars, totalRead, contentLength - totalRead);
                if (readBytes == -1) {
                    System.out.println("End of stream reached before reading all content.");
                    break;
                }
                totalRead += readBytes;
            }
            if (totalRead == contentLength) {
                body = new String(bodyChars);
            } else {
                System.out.printf("Incomplete body received! Expected %d, got %d%n", contentLength, totalRead);
            }
        }


        return new HttpRequest(method, requestTarget, headers, pathParameter,body);
    }



    }











