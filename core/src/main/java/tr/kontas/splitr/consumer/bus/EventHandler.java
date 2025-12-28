package tr.kontas.splitr.consumer.bus;

public interface EventHandler<T> extends BusHandler<T> {
    @Override
    default Object handle(T payload) {
        onEvent(payload);
        return null;
    }

    void onEvent(T payload);
}
