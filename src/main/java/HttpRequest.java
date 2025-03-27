import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    private String method;
    private String target;
    private String version;
    private Map<String, String> headers;
    private String body;
    private String pathParametar;

    public HttpRequest() {
        this.headers = new HashMap<>();
        this.setMethod("");
        this.setTarget("");
        this.setVersion("");
        this.setBody("");
    }
    public HttpRequest(String method, String target, Map<String, String> headers, String pathParametar,String body) {
        this.method = method;
        this.target = target;
        this.headers = headers;
        this.pathParametar = pathParametar;
        this.body = body;

    }

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

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getPathParametar() {
        return pathParametar;
    }

    public void setPathParametar(String pathParametar) {
        this.pathParametar = pathParametar;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", target='" + target + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", pathParametar='" + pathParametar + '\'' +
                '}';
    }
}