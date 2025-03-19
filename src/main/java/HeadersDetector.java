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
        if(headers.containsKey("If-None-Match"))
        {
            String headerValue = headers.get("If-None-Match");
            headerValue = headerValue.strip();
            int size = headerValue.length();
            if(size>1 && headerValue.charAt(size-1)=='"' && headerValue.charAt(size-1)=='"')
                return true;
        }
        return false;

    }
    public boolean detectAcceptEncoding(Map<String, String> headers)
    {

    }

    public boolean detectRange(Map<String, String> headers)
    {

    }
}
