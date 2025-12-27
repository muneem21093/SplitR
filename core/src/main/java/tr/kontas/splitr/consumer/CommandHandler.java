package tr.kontas.splitr.consumer;

/**
 * The fundamental interface for handling specific types of queries within the Splitr ecosystem.
 * <p>
 * Implementations of this interface are intended to be registered as Spring Beans.
 * The {@code commandDispatcher} uses these handlers to route incoming Kafka messages
 * to the appropriate business logic based on the class type.
 * </p>
 *
 * @param <T> The type of the command this handler supports. Usually and must be a serializable class.
 * @author BurakKontas
 * @version 1.0.0
 */
public interface CommandHandler<T> {

    /**
     * Identifies the specific class type of the command that this handler is capable of processing.
     * <p>
     * This is used by the dispatching mechanism to map incoming JSON payloads to the
     * correct handler implementation at runtime.
     * </p>
     *
     * @return The {@link Class} object representing the supported command type.
     */
    Class<T> type();

    /**
     * Processes the given command and returns the resulting data.
     * <p>
     * The returned object will typically be serialized back into a response message
     * and sent to the original requester via the configured callback mechanism.
     * </p>
     *
     * @param command The command object containing the request parameters.
     * @return The result of the command execution (Response DTO).
     * @throws InterruptedException If the processing thread is interrupted during execution.
     */
    Object handle(T command) throws InterruptedException;
}