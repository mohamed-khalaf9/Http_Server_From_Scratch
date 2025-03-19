import java.util.Map;

public class HeadersDetector {

    public boolean isPersistantConnection(Map<String, String> requestHeaders)
    {
        if(requestHeaders.containsKey("Connection"))
        {
            String headerValue = requestHeaders.get("Connection");
            if(headerValue.equalsIgnoreCase("keep-alive"))
                return true;
            else
                return false;
        }
        return false;

    }

    public boolean detectIfNoneMatch(Map<String, String> headers)
    {

    }
    public boolean detectAcceptEncoding(Map<String, String> headers)
    {

    }

    public boolean detectRange(Map<String, String> headers)
    {

    }
}
