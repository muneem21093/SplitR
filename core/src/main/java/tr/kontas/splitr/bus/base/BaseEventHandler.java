package tr.kontas.splitr.bus.base;

import tr.kontas.splitr.consumer.bus.EventHandler;

public abstract class BaseEventHandler<T> extends BaseHandler<T> implements EventHandler<T> {
    protected BaseEventHandler() {
        super();
    }
}
