package tr.kontas.splitr.bus.base;

import tr.kontas.splitr.consumer.bus.QueryHandler;

public abstract class BaseQueryHandler<T> extends BaseHandler<T> implements QueryHandler<T> {
    public BaseQueryHandler() {
        super();
    }
}
