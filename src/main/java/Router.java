import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Router {
    public Map<String,Map<String, Function<HttpRequest,HttpResponse>>> routes;

    public Router() {
        routes = new ConcurrentHashMap<>();
    }


    public void addRoute(String method, String path, Function<HttpRequest, HttpResponse> handler) {
        routes.computeIfAbsent(method, k -> new ConcurrentHashMap<>())
                .put(path, handler);
    }


}
