package tr.kontas.splitr.bus;

import tr.kontas.splitr.dto.CommandResponse;
import tr.kontas.splitr.dto.QueryResponse;

import java.util.concurrent.*;

public class SyncRegistry {
    private final ConcurrentHashMap<String, CompletableFuture<QueryResponse>> queryMap = new ConcurrentHashMap<>();

    public CompletableFuture<QueryResponse> registerQuery(String id) {
        var f = new CompletableFuture<QueryResponse>();
        queryMap.put(id, f);
        return f;
    }
    public void completeQuery(QueryResponse r) {
        var f = queryMap.remove(r.getId());
        if (f != null) f.complete(r);
    }

    private final ConcurrentHashMap<String, CompletableFuture<CommandResponse>> commandMap = new ConcurrentHashMap<>();

    public CompletableFuture<CommandResponse> registerCommand(String id) {
        var f = new CompletableFuture<CommandResponse>();
        commandMap.put(id, f);
        return f;
    }
    public void completeCommand(CommandResponse r) {
        var f = commandMap.remove(r.getId());
        if (f != null) f.complete(r);
    }
}
