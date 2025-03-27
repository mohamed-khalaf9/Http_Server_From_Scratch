import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public int port;
    public ServerSocket serverSocket;
    public Router router;
    public static final Map<String,RateLimiter> IP_RATE_LIMITER_MAP = new HashMap<>();


    public HttpServer(int port,Router router) {
        this.port = port;
        this.router = router;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        while (true){
            Socket socket = serverSocket.accept();
            String ipAddress = socket.getInetAddress().getHostAddress();
            IP_RATE_LIMITER_MAP.computeIfAbsent(ipAddress,k->new RateLimiter(20,60000));
            ClientHandler clientHandler = new ClientHandler(socket,router,ipAddress);
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.execute(clientHandler);
        }
    }


}