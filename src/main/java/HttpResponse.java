import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String version;
    private int statusCode;
    private int statusText;
    private Map<String, String> headers;
    private String body;

    public HttpResponse()
    {
        this.setVersion("Http1.1");
        this.headers = new HashMap<String, String>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusText() {
        return statusText;
    }

    public void setStatusText(int statusText) {
        this.statusText = statusText;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
}
