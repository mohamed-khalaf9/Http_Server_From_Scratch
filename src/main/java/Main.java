import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        Router router = new Router();
        Handlers handlers = new Handlers();

        // Register routes
        router.addRoute("POST", "/create", handlers::createFileHandler);
        router.addRoute("GET", "/file", request -> {
            try {
                return handlers.getFileHandler(request);
            } catch (IOException | NoSuchAlgorithmException e) {
                HttpResponse response = new HttpResponse();
                response.setStatusCode(500);
                response.setStatusText("Internal Server Error");
                response.setBody("An error occurred: " + e.getMessage());
                return response;
            }
        });
        router.addRoute("UPDATE", "/update", handlers::updateFileHandler);


        HttpServer server = new HttpServer(45321,router);




        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


