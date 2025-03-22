import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public int port;
    public ServerSocket serverSocket;
    public Router router;

    public HttpServer(int port,Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true){
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket,router);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.execute(clientHandler);
        }
    }


}