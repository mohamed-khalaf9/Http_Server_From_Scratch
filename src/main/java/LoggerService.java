import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

public class LoggerService {
    private static final Logger logger = LogManager.getLogger(LoggerService.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void logRequestAndResponse(HttpRequestLog requestLog, HttpResponseLog responseLog) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("request", Map.of(
                "requestId", requestLog.getRequestId(),
                "ipAddress", requestLog.getIpAddresse(),
                "timestamp", Utilities.formatTimestamp(requestLog.getTimestamp()),
                "httpRequest", requestLog.getHttpRequest()
        ));

        logEntry.put("response", Map.of(
                "responseId", responseLog.getResponseId(),
                "requestId", responseLog.getRequestId(),
                "clientIpAddress", responseLog.getClientIpAddresse(),
                "processingTime", Utilities.formatProcessingTime(responseLog.getProcessingTime()),
                "httpResponse", responseLog.getHttpResponse(),
                "resourceSize", responseLog.getResoureceSize()
        ));

        // Log as JSON string
        logger.info(gson.toJson(logEntry));
    }
}
