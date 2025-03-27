import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.function.Function;

public class ClientHandler implements  Runnable{
    private Socket clientSocket;
    private Router router;
    private static final Set<String> METHODS_WITH_BODY = Set.of("POST", "PUT", "PATCH");
    private long requestArrivalTime;


    ClientHandler(Socket socket,Router router)
    {
        this.clientSocket = socket;
        this.router = router;


    }


    @Override
    public void run() {
        BufferedReader in=null;
        BufferedWriter out=null;
        boolean keepAlive = true;
        System.out.println("Hello, world!");

        try {

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


            while (keepAlive) {
                clientSocket.setSoTimeout(60000);


                HttpRequest request = parseRequest(in);
                if (request == null) {
                    HttpResponse response = new HttpResponse();
                    response.setStatusCode(404);
                    response.setStatusText("Bad Request");
                    response.setBody("No request found");
                    Utilities.sendResponse(out, response);
                    keepAlive = false;
                    break;

                }
                System.out.println("Received Request: " + request.toString());

                Function<HttpRequest, HttpResponse> handler = router.getHandler(request.getMethod(), request.getTarget());
                try {
                    if (handler != null) {
                        HttpResponse response = handler.apply(request);
                        Utilities.sendResponse(out, response);// Send the response back to the client


                    } else {
                        HttpResponse response = new HttpResponse();
                        response.setStatusCode(404);
                        response.setStatusText("Handler Not Found");
                        Utilities.sendResponse(out, response);
                    }



                } catch (IOException e) {
                    System.out.println("error in request handler: "+e.getMessage());
                }


                HeadersDetector detector = new HeadersDetector();
                keepAlive = detector.isPersistantConnection(request.getHeaders());


            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            HttpResponse response = new HttpResponse();
            response.setStatusCode(500);
            response.setStatusText("Connection  closed");
            Utilities.sendResponse(out, response);
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        }





    public HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException, InterruptedException {
        String requestLine = bufferedReader.readLine();
        if(requestLine == null)
            return null;
        this.requestArrivalTime = System.currentTimeMillis();

        // Split method, target, version
        String[] requestLineParts = requestLine.split(" ", 3);
        if (requestLineParts.length < 3) {
            throw new IOException("Invalid request line");
        }
        String method = requestLineParts[0];

        String target = requestLineParts[1];
        String[] targetParts = target.split("/");

        String requestTarget = "/"+targetParts[1];// e.g., "/create/example.txt"
        String pathParameter = targetParts[2];

        // Read headers (ignore body)
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = bufferedReader.readLine()) != null && !headerLine.isEmpty()) {
            int colonIdx = headerLine.indexOf(':');
            if (colonIdx != -1) {
                String key = headerLine.substring(0, colonIdx).trim();
                String value = headerLine.substring(colonIdx + 1).trim();
                headers.put(key, value);
            }
        }

        // Extract the path parameter (e.g., "example.txt")
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











