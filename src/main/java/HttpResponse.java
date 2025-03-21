import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private String version;
    private int statusCode;
    private String statusText;
    private Map<String, String> headers;


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

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
    public Map<String, String> getHeaders() {
        return headers;
    }

}


