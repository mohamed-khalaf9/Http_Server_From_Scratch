import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
    private String version;
    private int statusCode;
    private String statusText;
    private Map<String, String> headers;
    private byte[] body;

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

    public byte[] getBody() {
        return this.body;
    }
    public void setBody(byte[] body) {
        this.body = body;
        this.headers.put("Content-Length", String.valueOf(body.length));
    }
    public void setBody(String body)
    {
        this.body = body.getBytes(StandardCharsets.UTF_8);
        this.headers.put("Content-Length", String.valueOf(this.body.length));
        this.headers.put("Content-Type", "text/plain; charset=UTF-8");

    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version='" + version + '\'' +
                ", statusCode=" + statusCode +
                ", statusText=" + statusText +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

    public String constructResponseFormat() {
        StringBuilder response = new StringBuilder();
        response.append(this.version)
                .append(" ")
                .append(this.statusCode)
                .append(" ")
                .append(this.statusText)
                .append("\r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        response.append("\r\n");
        response.append(Objects.toString(this.body, "")).append("\r\n");

        return response.toString();
    }

}

