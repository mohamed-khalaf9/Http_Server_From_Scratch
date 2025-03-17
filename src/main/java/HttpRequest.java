import java.util.Map;

public class HttpRequest {
    private String method;
    private String target;
    private String version;
    private Map<String, String> headers;
    private String body;


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
    public void addHeader(String key, String value)
    {
        headers.put(key, value);
    }
}
