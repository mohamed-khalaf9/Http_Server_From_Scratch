import java.util.HashMap;
import java.util.Map;


public class HttpResponse {
    private String version;
    private int statusCode;
    private String statusText;
    private Map<String, String> headers;
    private String body;


    public HttpResponse()
    {
        this.setVersion("HTTP/1.1");
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getRawResponse(){

            StringBuilder response = new StringBuilder();
            // write response line
            response.append(version).append(" ").append(statusCode).append(" ").append(statusText).append("\r\n");
            // write headers
            for (Map.Entry<String, String> header : headers.entrySet()) {
                response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
            }

            response.append("\r\n");

            if (body != null) {
                response.append(body);
            }

            return response.toString();



    }

}


