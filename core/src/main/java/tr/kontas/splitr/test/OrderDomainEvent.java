package tr.kontas.splitr.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import tr.kontas.splitr.bus.domainevent.DomainEvent;

@Data
@AllArgsConstructor
public class OrderDomainEvent implements DomainEvent {
    private String id;
}
