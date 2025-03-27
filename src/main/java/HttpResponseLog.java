import java.util.UUID;

public class HttpResponseLog {
    private String responseId;
    private String requestId;
    private String clientIpAddresse;
    private long processingTime;
    private HttpResponse httpResponse;
    private int resoureceSize;

    public HttpResponseLog(String requestId,String clientIpAddresse,long processingTime,HttpResponse httpResponse) {
        this.responseId = UUID.randomUUID().toString();
        this.requestId = requestId;
        this.clientIpAddresse = clientIpAddresse;
        this.processingTime = processingTime;
        this.httpResponse = httpResponse;
        this.resoureceSize = httpResponse.getBody() != null ? httpResponse.getBody().length():0;
    }
    public HttpResponseLog(){

    }

}
