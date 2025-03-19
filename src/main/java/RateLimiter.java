import java.util.LinkedList;
import java.util.Queue;

public class RateLimiter {
    private int maxRequests;
    private long maxWindowSize;
    private Queue<Long> requestslog;


    public RateLimiter(int maxRequests, long maxWindowSize) {
        this.maxRequests = maxRequests;
        this.maxWindowSize = maxWindowSize;
        this.requestslog = new LinkedList<Long>();
    }


}
