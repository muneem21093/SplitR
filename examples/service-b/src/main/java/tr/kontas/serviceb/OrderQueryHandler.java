package tr.kontas.serviceb;

import org.springframework.stereotype.Component;
import tr.kontas.splitr.consumer.bus.QueryHandler;
import tr.kontas.splitr.test.OrderQuery;

@Component
public class OrderQueryHandler implements QueryHandler<OrderQuery> {
    @Override public Class<OrderQuery> type() { return OrderQuery.class; }

    @Override
    public Object handle(OrderQuery q) {
        return "DATA-FOR-" + q.orderId();
    }
}

