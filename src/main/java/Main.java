import java.io.IOException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        Router router = new Router();
        Handlers handlers = new Handlers();

        // Register routes
        router.addRoute("POST", "/create", handlers::createFileHandler);
        router.addRoute("GET", "/file", handlers::getFileHandler);
        HttpServer server = new HttpServer(45321,router);




        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


