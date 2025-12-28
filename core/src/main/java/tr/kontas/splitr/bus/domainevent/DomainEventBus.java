package tr.kontas.splitr.bus.domainevent;


/**
 * Provides a contract for publishing domain events within the application.
 * <p>
 * This interface abstracts the event dispatch mechanism for in-memory,
 * fire-and-forget domain events. Events published via this bus do not expect
 * a response and are handled immediately within the same JVM.
 * </p>
 * <p>
 * The method {@link #arise(DomainEvent)} is used to dispatch events.
 * Normally, such methods are called {@code raise}, but here it is named
 * {@code arise} as a playful nod to Solo Leveling. Functionally, it behaves
 * the same as a conventional raise.
 * </p>
 * <p>
 * Use this bus for domain-driven events that should be processed locally
 * and immediately without involving external transport layers.
 * </p>
 *
 * @author BurakKontas
 * @version 1.0.0
 */
public interface DomainEventBus {

    /**
     * Publishes a domain event in a fire-and-forget manner without blocking the caller.
     * <p>
     * This method dispatches the event immediately within the same JVM (in-memory),
     * suitable for domain events that do not require external transport or response handling.
     * </p>
     * <p>
     * Normally, such methods are called {@code raise}, but here it is named {@code arise}
     * as a playful nod to Solo Leveling. No special technical difference — it behaves just like a regular raise.
     * </p>
     *
     * @param event The domain event to be dispatched.
     */
    void arise(DomainEvent event);
}