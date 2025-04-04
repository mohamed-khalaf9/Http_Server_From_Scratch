import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class Handlers {

    public synchronized HttpResponse createFileHandler(HttpRequest request)  {
        HttpResponse response = new HttpResponse();
        ETagManager eTagManager = ETagManager.getInstance();




        String fileName = request.getPathParametar().trim();
        if (fileName.isEmpty()) {
            response.setStatusCode(400);
            response.setStatusText("Bad Request");
            response.setBody("File name is missing");
            return response;
        }


        File file = new File(fileName);
        try{
            if(file.createNewFile())
            {




                String etag = eTagManager.generateETag(fileName);
                response.setStatusCode(201);
                response.setStatusText("Created");
                response.setBody("File Created");
                response.addHeader("ETag",etag);
                return response;
            }
            else
            {
                response.setStatusCode(409);
                response.setStatusText("Conflict");
                response.setBody("File Already Exists");
                response.addHeader("ETag",eTagManager.getFileEtag(fileName));
                return response;
            }
        }
        catch(IOException e) {
            response.setStatusCode(500);
            response.setStatusText("Internal Server Error");
            response.setBody("Failed to Create File");
            return response;
        }

    }
    public synchronized HttpResponse getFileHandler(HttpRequest request) throws IOException, NoSuchAlgorithmException {
        HttpResponse response = new HttpResponse();
        ETagManager eTagManager = ETagManager.getInstance();
        HeadersDetector detector = new HeadersDetector();
        RangeRequestHandler rangeHandler = new RangeRequestHandler();


        String fileName = request.getPathParametar().trim();
        if (fileName.isEmpty()) {
            response.setStatusCode(400);
            response.setStatusText("Bad Request");
            response.setBody("File name is missing");
            return response;
        }

        File file = new File(fileName);
        if(!file.exists())
        {
            response.setStatusCode(404);
            response.setStatusText("Not Found");
            response.setBody("File Not Found");
            return response;
        }

        boolean ifNonMatchHeaderExist = detector.detectIfNoneMatch(request.getHeaders());
        if (ifNonMatchHeaderExist) {
            boolean ifMatch = eTagManager.compare(fileName, request.getHeaders().get("If-None-Match").trim());
            if (ifMatch) {
                response.setStatusCode(304);
                response.setStatusText("Not Modified");
                response.setBody("Not Modified");
                response.addHeader("ETag", eTagManager.getFileEtag(fileName));
                return response;
            }
        }

        byte[] fileContent = null;

        boolean isRangeRequest = detector.detectRange(request.getHeaders());
        if (isRangeRequest) {
            long[] rangeValues = Utilities.parseRange(request.getHeaders().get("Range"), file.length());
            long start = rangeValues[0];
            long end = rangeValues[1];

            // If range parsing failed, return the whole file
            if (start == 0 && end == 0) {
                isRangeRequest = false;
            } else {
                try {
                    fileContent = rangeHandler.handleRangeRequest(fileName, start, end);
                    response.setStatusCode(206);
                    response.setStatusText("Partial Content");
                    response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + file.length());
                } catch (IOException e) {
                    isRangeRequest = false; // Fall back to full file if range handling fails
                }
            }
        }

        if (!isRangeRequest) {
            try (FileInputStream fis = new FileInputStream(file)) { // can we make it bufferd?
                fileContent = fis.readAllBytes();
            } catch (IOException e) {
                response.setStatusCode(500);
                response.setStatusText("Internal Server Error");
                response.setBody("Failed to read file");
                return response;
            }
        }
        boolean supportsGzip = detector.detectAcceptEncoding(request.getHeaders());
        if (supportsGzip) {
            try {

                fileContent = GZipCompressor.compressFile(fileContent);
                response.addHeader("Content-Encoding", "gzip");
            } catch (IOException e) {
                response.setStatusCode(500);
                response.setStatusText("Internal Server Error");
                response.setBody("Failed to compress file");
                return response;
            }
        }

        response.setStatusCode(isRangeRequest ? 206 : 200);
        response.setStatusText(isRangeRequest ? "Partial Content" : "OK");
        response.setBody(new String(fileContent, StandardCharsets.UTF_8)); // For text files
        response.addHeader("Content-Length", String.valueOf(fileContent.length));
        response.addHeader("ETag", eTagManager.generateETag(fileName));

        return response;

    }
    public synchronized HttpResponse updateFileHandler(HttpRequest request)  {
       HttpResponse response = new HttpResponse();
        ETagManager eTagManager = ETagManager.getInstance();
       String fileName="";
       String fileContent= request.getBody();

       fileName = request.getPathParametar().trim();
       if (fileName.isEmpty()) {
           response.setStatusCode(400);
           response.setStatusText("Bad Request");
           response.setBody("File name is missing");
           return response;
       }
       if(fileContent.isEmpty())
       {
           response.setStatusCode(400);
           response.setStatusText("Bad Request");
           response.setBody("Content to be added is missing");
           return response;
       }
       File file = new File(fileName);
       if(!file.exists())
       {
           response.setStatusCode(404);
           response.setStatusText("Not Found");
           response.setBody("File Not Found");
           return response;
       }
       try(FileWriter fw = new FileWriter(fileName,true);)
       {
           fw.write("\n");
           fw.write(fileContent);
           fw.flush();
           fw.close();
           response.setStatusCode(200);
           response.setStatusText("OK");
           response.setBody("Content added successfully");
           String etag = eTagManager.generateETag(fileName);
           response.addHeader("ETag",etag);
           return response;

       }
       catch(IOException e) {
           response.setStatusCode(500);
           response.setStatusText("Internal Server Error");
           response.setBody("Internal Server Error");
           return response;
       }

    }
    public synchronized HttpResponse pingHandler(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(200);
        response.setStatusText("OK");
        response.setBody("Pong");
        return response;

    }
}
