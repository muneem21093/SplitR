package tr.kontas.splitr.consumer;

import tr.kontas.splitr.dto.QueryResponse;
import tr.kontas.splitr.dto.base.BaseResponse;

import java.util.*;

public class LruStore implements IdempotencyStore {

    private final Map<String, BaseResponse> cache;

    public LruStore(int max) {
        cache = new LinkedHashMap<>(16,0.75f,true) {
            protected boolean removeEldestEntry(Map.Entry<String,BaseResponse> e) {
                return size() > max;
            }
        };
    }
    public boolean contains(String id){ return cache.containsKey(id); }
    public BaseResponse get(String id){ return cache.get(id); }
    public void put(String id, BaseResponse r){ cache.put(id,r); }
}