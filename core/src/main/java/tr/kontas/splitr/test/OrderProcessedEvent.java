package tr.kontas.splitr.test;

import tr.kontas.splitr.bus.event.Event;

public record OrderProcessedEvent(String orderId) implements Event {
}
