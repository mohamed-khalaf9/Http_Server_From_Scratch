import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public int port;
    public ServerSocket serverSocket;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true){
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.execute(clientHandler);
        }
    }


}