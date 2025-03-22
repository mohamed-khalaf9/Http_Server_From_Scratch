import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Router {
    public Map<String,Map<String, Function<HttpRequest, HttpResponse>>> routes;

    public Router() {
        routes = new ConcurrentHashMap<>();
    }


    public void addRoute(String method, String path, Function<HttpRequest, HttpResponse> handler) {
        routes.computeIfAbsent(method, k -> new ConcurrentHashMap<>())
                .put(path, handler);
    }

    public Optional<Function<HttpRequest, HttpResponse>> getHandler(String method, String target)
    {
        Map<String,Function<HttpRequest, HttpResponse>> handlers = routes.get(method);
        if(handlers != null)
        {
            return Optional.ofNullable(handlers.get(target));
        }

        return Optional.empty();

    }


}
