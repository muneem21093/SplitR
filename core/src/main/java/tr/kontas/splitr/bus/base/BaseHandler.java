package tr.kontas.splitr.bus.base;

import org.springframework.core.GenericTypeResolver;
import tr.kontas.splitr.consumer.bus.BusHandler;

public abstract class BaseHandler<T> implements BusHandler<T> {

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected BaseHandler() {
        this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseHandler.class);
        if (this.type == null) {
            throw new IllegalStateException("Could not resolve generic type for " + getClass().getName());
        }
    }

    @Override
    public Class<T> type() {
        return type;
    }
}
