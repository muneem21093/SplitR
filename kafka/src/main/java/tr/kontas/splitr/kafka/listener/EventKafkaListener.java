package tr.kontas.splitr.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import tr.kontas.splitr.consumer.dispatcher.EventDispatcher;
import tr.kontas.splitr.dto.EventRequest;

@RequiredArgsConstructor
@Slf4j
public class EventKafkaListener {

    private final EventDispatcher dispatcher;

    @KafkaListener(
            topics = "${splitr.bus.kafka.event.topic:tr.kontas.splitr.event.topic}",
            groupId = "${splitr.bus.kafka.event.consumer:tr.kontas.splitr.event.consumer}"
    )
    public void listen(EventRequest r) throws Exception {
        log.atInfo().log("Dispatching command: " + r.getId());
        dispatcher.dispatch(r);
    }
}