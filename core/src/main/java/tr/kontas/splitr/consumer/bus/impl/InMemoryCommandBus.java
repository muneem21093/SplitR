package tr.kontas.splitr.consumer.bus.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import tr.kontas.splitr.bus.command.Command;
import tr.kontas.splitr.bus.command.CommandBus;
import tr.kontas.splitr.consumer.dispatcher.CommandDispatcher;
import tr.kontas.splitr.consumer.store.IdempotencyStore;
import tr.kontas.splitr.dto.CommandRequest;

import java.util.concurrent.CompletableFuture;

public class InMemoryCommandBus implements CommandBus {

    private final CommandDispatcher dispatcher;
    private final ObjectMapper mapper;
    private final IdempotencyStore store;

    public InMemoryCommandBus(CommandDispatcher dispatcher, ObjectMapper mapper, IdempotencyStore store) {
        this.dispatcher = dispatcher;
        this.mapper = mapper;
        this.store = store;
    }

    @Override
    public <T> T publishSync(Command command, Class<T> responseType, long timeoutMs) {
        try {
            var req = new CommandRequest(
                    java.util.UUID.randomUUID().toString(),
                    command.getClass().getName(),
                    mapper.writeValueAsString(command),
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
    public <T> T publishSync(Command command, Class<T> responseType) {
        return publishSync(command, responseType, Long.MAX_VALUE);
    }

    @Override
    public <T> CompletableFuture<T> publishAsync(Command command, Class<T> responseType) {
        return CompletableFuture.supplyAsync(() -> publishSync(command, responseType));
    }

    @Override
    public void publish(Command command) {
        publishSync(command, Void.class);
    }

    @Override
    public void publish(Command command, long timeoutMs) {
        publishSync(command, Void.class, timeoutMs);
    }
}
