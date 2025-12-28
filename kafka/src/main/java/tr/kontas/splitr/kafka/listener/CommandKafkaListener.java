package tr.kontas.splitr.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import tr.kontas.splitr.consumer.dispatcher.CommandDispatcher;
import tr.kontas.splitr.dto.CommandRequest;

@RequiredArgsConstructor
@Slf4j
public class CommandKafkaListener {

    private final CommandDispatcher dispatcher;

    @KafkaListener(
            topics = "${splitr.bus.kafka.command.topic:tr.kontas.splitr.command.topic}",
            groupId = "${splitr.bus.kafka.command.consumer:tr.kontas.splitr.command.consumer}"
    )
    public void listen(CommandRequest r) throws Exception {
        log.atInfo().log("Dispatching command: " + r.getId());
        dispatcher.dispatch(r);
    }
}