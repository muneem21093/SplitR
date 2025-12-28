package tr.kontas.splitr.test;

import tr.kontas.splitr.bus.query.Query;

public record OrderQuery(String orderId) implements Query {
}
