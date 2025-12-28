package tr.kontas.splitr.kafka.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import tr.kontas.splitr.bus.event.Event;
import tr.kontas.splitr.bus.event.EventBus;
import tr.kontas.splitr.bus.registry.SyncRegistry;
import tr.kontas.splitr.dto.EventRequest;
import tr.kontas.splitr.kafka.bus.base.AbstractKafkaBus;

public class KafkaEventBus extends AbstractKafkaBus<EventRequest> implements EventBus {

    public KafkaEventBus(String commandTopic, KafkaTemplate<String, EventRequest> kafka,
                         SyncRegistry registry, ObjectMapper mapper,
                         String callbackUrl, int defaultTimeout) {
        super(commandTopic, kafka, registry, mapper, callbackUrl, defaultTimeout);
    }

    @Override
    protected EventRequest createRequest(String id, String typeName, String payload, boolean isSync, long now, long timeout) {
        return new EventRequest(id, typeName, payload);
    }

    @Override
    public void publish(Event event) {
        execute(event);
    }
}