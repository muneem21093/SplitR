package tr.kontas.splitr.consumer.domainevent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tr.kontas.splitr.consumer.dispatcher.DomainEventDispatcher;
import tr.kontas.splitr.dto.DomainEventRequest;

@RequiredArgsConstructor
@Slf4j
public class DomainEventListener {

    private final DomainEventDispatcher dispatcher;

    public void listen(DomainEventRequest r) {
        log.atInfo().log("Dispatching event: " + r.getId());
        dispatcher.dispatch(r);
    }
}