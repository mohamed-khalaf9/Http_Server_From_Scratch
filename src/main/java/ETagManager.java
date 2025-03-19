import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ETagManager {
    private Map<String,String> fileEtags;

    public ETagManager()
    {
        fileEtags = new ConcurrentHashMap<>();
    }

}
