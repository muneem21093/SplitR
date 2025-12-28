package tr.kontas.splitr.bus.base;

import tr.kontas.splitr.consumer.bus.CommandHandler;

public abstract class BaseCommandHandler<T> extends BaseHandler<T> implements CommandHandler<T> {
    public BaseCommandHandler() {
        super();
    }
}
