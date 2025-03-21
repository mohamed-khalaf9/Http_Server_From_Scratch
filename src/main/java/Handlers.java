import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Handlers {

    public synchronized HttpResponse createFileHandler(HttpRequest request)  {
        HttpResponse response = new HttpResponse();
        ETagManager etagManager = new ETagManager();



        String fileName = request.getBody().trim();
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

                String etag = etagManager.generateEtag(fileName);
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
                response.addHeader("ETag",etagManager.getFileEtag(fileName));
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
    public synchronized HttpResponse getFileHandler(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        ETagManager etagManager = new ETagManager();
        HeadersDetector detector = new HeadersDetector();

        String fileName = request.getBody().trim();
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
            boolean ifMatch = etagManager.compare(fileName, request.getHeaders().get("If-None-Match"));
            if (ifMatch) {
                response.setStatusCode(304);
                response.setStatusText("Not Modified");
                response.setBody("Not Modified");
                response.addHeader("ETag", etagManager.getFileEtag(fileName));
                return response;
            }
        }












    }
    public synchronized HttpResponse updateFileHandler(HttpRequest request) {


    }
}
