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
    public HttpRequestLog() {

    }

    public String getRequestId() {
        return requestId;
    }

    public String getIpAddresse() {
        return ipAddresse;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }
}
