package tr.kontas.splitr.bus.event;


/**
 * Provides a contract for publishing events across the distributed system.
 * <p>
 * This interface abstracts the underlying transport mechanism, allowing callers to
 * dispatch events in a fire-and-forget manner without expecting a response.
 * </p>
 *
 * @author BurakKontas
 * @version 1.0.0
 */
public interface EventBus {

    /**
     * Publishes an event without blocking (fire-and-forget).
     * <p>
     * No response or acknowledgement is expected from the bus.
     * </p>
     *
     * @param event The event payload object to be dispatched.
     */
    void publish(Event event);
}