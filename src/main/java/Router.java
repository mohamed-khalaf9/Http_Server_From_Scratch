import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Router {
    public Map<String,Map<String, Function<HttpRequest,HttpResponse>>> routes;

    public Router()
    {
        routes = new ConcurrentHashMap<>();
    }



}
