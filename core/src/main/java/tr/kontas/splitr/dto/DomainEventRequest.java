package tr.kontas.splitr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tr.kontas.splitr.bus.domainevent.DomainEvent;

@Data
@AllArgsConstructor
public class DomainEventRequest {
    private String id;
    private String type;
    private DomainEvent payload;
}
