package tr.kontas.splitr.consumer.bus.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import tr.kontas.splitr.bus.query.Query;
import tr.kontas.splitr.bus.query.QueryBus;
import tr.kontas.splitr.consumer.dispatcher.QueryDispatcher;
import tr.kontas.splitr.consumer.store.IdempotencyStore;
import tr.kontas.splitr.dto.QueryRequest;

import java.util.concurrent.CompletableFuture;

public class InMemoryQueryBus implements QueryBus {

    private final QueryDispatcher dispatcher;
    private final ObjectMapper mapper;
    private final IdempotencyStore store;

    public InMemoryQueryBus(QueryDispatcher dispatcher, ObjectMapper mapper, IdempotencyStore store) {
        this.dispatcher = dispatcher;
        this.mapper = mapper;
        this.store = store;
    }

    @Override
    public <T> T publishSync(Query query, Class<T> responseType, long timeoutMs) {
        try {
            var req = new QueryRequest(
                    java.util.UUID.randomUUID().toString(),
                    query.getClass().getName(),
                    mapper.writeValueAsString(query),
                    "", // dummy callback
                    true,
                    System.currentTimeMillis(),
                    timeoutMs
            );
            dispatcher.dispatch(req);
            var resp = store.get(req.getId());
            if (resp == null) return null;
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(resp.getResult(), responseType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T publishSync(Query query, Class<T> responseType) {
        return publishSync(query, responseType, Long.MAX_VALUE);
    }

    @Override
    public <T> CompletableFuture<T> publishAsync(Query query, Class<T> responseType) {
        return CompletableFuture.supplyAsync(() -> publishSync(query, responseType));
    }
}
