import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Router {
    public static Map<String,Map<String, Function<HttpRequest, HttpResponse>>> routes;

    public Router() {
        routes = new ConcurrentHashMap<>();
    }


    public void addRoute(String method, String path, Function<HttpRequest, HttpResponse> handler) {
        routes.computeIfAbsent(method, k -> new ConcurrentHashMap<>())
                .put(path, handler);
    }

    public Function<HttpRequest, HttpResponse> getHandler(String method, String target) {
        Map<String, Function<HttpRequest, HttpResponse>> handlers = routes.get(method);
        if (handlers != null) {
            return handlers.get(target); // Returns null if not found
        }
        return null;
    }

}
