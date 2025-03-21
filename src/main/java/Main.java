import java.io.IOException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
     HttpServer server = new HttpServer(45321);
     try{
         server.start();
     }
     catch(IOException e){
         e.printStackTrace();
     }


    }
}
