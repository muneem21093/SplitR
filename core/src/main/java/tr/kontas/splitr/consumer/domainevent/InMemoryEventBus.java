package tr.kontas.splitr.consumer.domainevent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tr.kontas.splitr.bus.domainevent.DomainEvent;
import tr.kontas.splitr.bus.domainevent.DomainEventBus;
import tr.kontas.splitr.consumer.dispatcher.DomainEventDispatcher;
import tr.kontas.splitr.dto.DomainEventRequest;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class InMemoryEventBus implements DomainEventBus {

    private final DomainEventDispatcher domainEventDispatcher;

    @Override
    public void arise(DomainEvent event) {
        try {
            String id = UUID.randomUUID().toString();
            DomainEventRequest request = new DomainEventRequest(
                    id,
                    event.getClass().getName(),
                    event
            );

            domainEventDispatcher.dispatch(request);
        } catch (Exception e) {
            log.error("In-memory event dispatch failed", e);
            throw new RuntimeException(e);
        }
    }
}
