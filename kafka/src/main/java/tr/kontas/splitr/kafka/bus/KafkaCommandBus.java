package tr.kontas.splitr.kafka.bus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import tr.kontas.splitr.bus.CommandBus;
import tr.kontas.splitr.bus.SyncRegistry;
import tr.kontas.splitr.dto.CommandRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class KafkaCommandBus implements CommandBus {

    private final KafkaTemplate<String, CommandRequest> kafka;
    private final SyncRegistry registry;
    private final ObjectMapper mapper;
    private final String callbackUrl;
    private final String commandTopic;
    private final int defaultTimeout;

    public KafkaCommandBus(String commandTopic,
                         KafkaTemplate<String, CommandRequest> kafka,
                         SyncRegistry registry,
                         ObjectMapper mapper,
                         String callbackUrl,
                         int defaultTimeout) {
        this.commandTopic = commandTopic;
        this.kafka = kafka;
        this.registry = registry;
        this.mapper = mapper;
        this.callbackUrl = callbackUrl;
        this.defaultTimeout = defaultTimeout;

        if(callbackUrl.isBlank()){
            throw new RuntimeException("splitr.callback-url is blank");
        }
    }

    @Override
    public <T> T publishSync(Object command, Class<T> responseType, long timeoutMs) {
        try {
            String id = UUID.randomUUID().toString();
            long now = System.currentTimeMillis();

            var future = registry.registerQuery(id);

            kafka.send(this.commandTopic, id,
                    new CommandRequest(
                            id,
                            command.getClass().getName(),
                            mapper.writeValueAsString(command),
                            callbackUrl,
                            true,
                            now,
                            timeoutMs
                    )
            );

            return mapper.readValue(
                    future.get(timeoutMs, TimeUnit.MILLISECONDS).getResult(),
                    responseType
            );
        } catch (Exception e) {
            throw new RuntimeException("Command timeout", e);
        }
    }

    @Override
    public <T> T publishSync(Object command, Class<T> responseType) {
        return publishSync(command, responseType, defaultTimeout);
    }

    @Override
    public <T> CompletableFuture<T> publishAsync(Object command, Class<T> responseType) {
        try {
            String id = UUID.randomUUID().toString();
            long now = System.currentTimeMillis();

            var registryFuture = registry.registerCommand(id);

            kafka.send(this.commandTopic, id,
                new CommandRequest(
                        id,
                        command.getClass().getName(),
                        mapper.writeValueAsString(command),
                        callbackUrl,
                        false,
                        now,
                        Long.MAX_VALUE
                )
            );

            return registryFuture.thenApply(response -> {
                try {
                    return mapper.readValue(response.getResult(), responseType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            CompletableFuture<T> failed = new CompletableFuture<>();
            failed.completeExceptionally(e);
            return failed;
        }
    }

    @Override
    public void publish(Object command) {
        publish(command, Long.MAX_VALUE);
    }

    @Override
    public void publish(Object command, long timeoutMs) {
        try {
            String id = UUID.randomUUID().toString();
            long now = System.currentTimeMillis();

            // Kafka’ya fire-and-forget komut publish
            kafka.send(this.commandTopic, id,
                    new CommandRequest(
                            id,
                            command.getClass().getName(),
                            mapper.writeValueAsString(command),
                            callbackUrl,
                            false,
                            now,
                            timeoutMs
                    )
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}