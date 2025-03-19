import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String target;
    private String version;
    private Map<String, String> headers;
    private String body;

    public HttpRequest() {
        this.headers = new HashMap<>();
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

    public HttpRequest parseRequest(String line) {
        // fetch request components based on crlf \r\n (request line,headers,body)
        HttpRequest parsedRequest = new HttpRequest();

        String[] tokens = line.split("\r\n", -1);

        String requestLine = tokens[0].stripLeading();
        parsedRequest.parseRequestLine(requestLine);

        int index = 1;
        while(index <tokens.length && !tokens[index].isEmpty())
        {
            String header = tokens[index].strip();
            String[] headerTokens = header.split(":", 2);
            if(headerTokens.length == 2)
            {
                parsedRequest.addHeader(headerTokens[0], headerTokens[1]);
            }
            ++index;
        }

        if(index +1 < tokens.length)
        {
            parsedRequest.setBody(String.join("\r\n", Arrays.copyOfRange(tokens,index+1,tokens.length)));
        }
        else
            parsedRequest.setBody("");

        return parsedRequest;

    }

}
