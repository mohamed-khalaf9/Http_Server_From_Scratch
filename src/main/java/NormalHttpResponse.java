import java.util.Map;

public class NormalHttpResponse extends HttpResponse {
    private String body;

    public NormalHttpResponse() {
        super();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String constructResponseFormat() {
        StringBuilder response = new StringBuilder();
        response.append(super.getVersion())
                .append(" ")
                .append(super.getStatusCode())
                .append(" ")
                .append(super.getStatusText())
                .append("\r\n");
        for (Map.Entry<String, String> entry : super.getHeaders().entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        response.append("\r\n");
        response.append(body);
        return response.toString();
    }
}