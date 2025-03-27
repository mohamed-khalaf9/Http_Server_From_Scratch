import java.util.UUID;

public class HttpRequestLog {
    private String requestId;
    private String ipAddresse;
    private long timestamp;
    private HttpRequest httpRequest;

    public HttpRequestLog(HttpRequest httpRequest,String ipAddresse,long timestamp) {
        this.requestId = UUID.randomUUID().toString();
        this.ipAddresse = ipAddresse;
        this.timestamp = timestamp;
        this.httpRequest = httpRequest;
    }



}
