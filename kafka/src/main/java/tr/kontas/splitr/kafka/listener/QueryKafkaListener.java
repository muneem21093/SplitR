package tr.kontas.splitr.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import tr.kontas.splitr.consumer.dispatcher.QueryDispatcher;
import tr.kontas.splitr.dto.QueryRequest;

@RequiredArgsConstructor
@Slf4j
public class QueryKafkaListener {

    private final QueryDispatcher dispatcher;

    @KafkaListener(
            topics = "${splitr.bus.kafka.query.topic:tr.kontas.splitr.query.topic}",
            groupId = "${splitr.bus.kafka.query.consumer:tr.kontas.splitr.query.consumer}"
    )
    public void listen(QueryRequest r) throws Exception {
        log.atInfo().log("Dispatching query: " + r.getId());
        dispatcher.dispatch(r);
    }
}