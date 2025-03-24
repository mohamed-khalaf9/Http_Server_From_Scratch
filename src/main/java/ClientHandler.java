import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

public class ClientHandler implements  Runnable{
    private Socket clientSocket;
    private Router router;
    private static final Set<String> METHODS_WITH_BODY = Set.of("POST", "PUT", "PATCH");

    ClientHandler(Socket socket,Router router)
    {
        this.clientSocket = socket;
        this.router = router;
    }
    @Override
    public void run() {
        System.out.println("Hello, world!");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            HttpRequest request = parseRequest(in);
            if (request == null) {
                HttpResponse response = new HttpResponse();
                response.setStatusCode(404);
                response.setStatusText("Bad Request");
                sendResponse(out, response);
                return;
            }

            System.out.println("Received Request: " + request.toString());


            Function<HttpRequest, HttpResponse> handler = router.getHandler(request.getMethod(), request.getTarget());

            try {
                if (handler != null) {
                    HttpResponse response = handler.apply(request);
                    sendResponse(out, response); // Send the response back to the client
                } else {
                    HttpResponse response = new HttpResponse();
                    response.setStatusCode(404);
                    response.setStatusText("Handler Not Found");
                    sendResponse(out, response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequest parseRequest(BufferedReader bufferedReader) throws IOException {
        // Read request line
        String requestLine = bufferedReader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Empty request line");
        }

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


    private void sendResponse(BufferedWriter out, HttpResponse response) throws IOException {
        out.write(response.getRawResponse());
        out.newLine();
    }

    }










