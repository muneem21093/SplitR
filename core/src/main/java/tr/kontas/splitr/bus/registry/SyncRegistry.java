package tr.kontas.splitr.bus.registry;

import tr.kontas.splitr.dto.base.BaseResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SyncRegistry {
    private final ConcurrentHashMap<String, CompletableFuture<BaseResponse>> map = new ConcurrentHashMap<>();

    public CompletableFuture<BaseResponse> register(String id) {
        var f = new CompletableFuture<BaseResponse>();
        map.put(id, f);
        return f;
    }

    public void complete(BaseResponse r) {
        var f = map.remove(r.getId());
        if (f != null) f.complete(r);
    }
}
