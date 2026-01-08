package tr.kontas.splitr.consumer.bus.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import tr.kontas.splitr.bus.event.Event;
import tr.kontas.splitr.bus.event.EventBus;
import tr.kontas.splitr.consumer.dispatcher.EventDispatcher;
import tr.kontas.splitr.dto.EventRequest;

public class InMemoryEventBus implements EventBus {

    private final EventDispatcher dispatcher;
    private final ObjectMapper mapper;

    public InMemoryEventBus(EventDispatcher dispatcher, ObjectMapper mapper) {
        this.dispatcher = dispatcher;
        this.mapper = mapper;
    }

    @Override
    public void publish(Event event) {
        try {
            var req = new EventRequest(
                    java.util.UUID.randomUUID().toString(),
                    event.getClass().getName(),
                    mapper.writeValueAsString(event)
            );
            dispatcher.dispatch(req);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
