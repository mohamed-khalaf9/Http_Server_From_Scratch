import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.Function;

public class RequestHandler implements Runnable{
    private HttpRequest request;
    private HttpRequestLog requestLog;
    private Router router;
    private BufferedWriter out;
    private ClientHandler clientHandler;


    public RequestHandler(HttpRequest request, HttpRequestLog requestLog, Router router, ClientHandler clientHandler, BufferedWriter out) {
        this.request = request;
        this.requestLog = requestLog;
        this.router = router;
        this.clientHandler = clientHandler;
        this.out = out;
    }





    @Override
    public void run() {

        Function<HttpRequest, HttpResponse> handler = router.getHandler(request.getMethod(), request.getTarget());
        try {
            if (handler != null) {
                HttpResponse response = handler.apply(request);
                Utilities.sendResponse(out, response); // Send the response back to the client
            } else {
                HttpResponse response = new HttpResponse();
                response.setStatusCode(404);
                response.setStatusText("Handler Not Found");
                Utilities.sendResponse(out, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
