package tr.kontas.splitr.bus.base;

import org.springframework.core.GenericTypeResolver;
import tr.kontas.splitr.consumer.bus.DomainEventHandler;


public abstract class BaseDomainEventHandler<T> implements DomainEventHandler<T> {
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    protected BaseDomainEventHandler() {
        this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseDomainEventHandler.class);
        if (this.type == null) {
            throw new IllegalStateException("Could not resolve generic type for " + getClass().getName());
        }
    }

    @Override
    public Class<T> type() {
        return type;
    }
}
