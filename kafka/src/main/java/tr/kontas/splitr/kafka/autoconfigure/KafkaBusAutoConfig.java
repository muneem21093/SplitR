package tr.kontas.splitr.kafka.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.KafkaTemplate;
import tr.kontas.splitr.bus.command.CommandBus;
import tr.kontas.splitr.bus.command.CommandCallbackController;
import tr.kontas.splitr.bus.query.QueryBus;
import tr.kontas.splitr.bus.query.QueryCallbackController;
import tr.kontas.splitr.bus.registry.SyncRegistry;
import tr.kontas.splitr.dto.CommandRequest;
import tr.kontas.splitr.dto.QueryRequest;
import tr.kontas.splitr.kafka.bus.KafkaCommandBus;
import tr.kontas.splitr.kafka.bus.KafkaQueryBus;

@Configuration
@ConditionalOnProperty(name = "splitr.publisher.enabled", havingValue = "true")
public class KafkaBusAutoConfig {

    @Bean
    public SyncRegistry syncRegistry() {
        return new SyncRegistry();
    }

    @Bean
    public QueryCallbackController queryCallbackController(SyncRegistry registry) {
        return new QueryCallbackController(registry);
    }

    @Bean
    public QueryBus queryBus(
            KafkaTemplate<String, QueryRequest> kafka,
            SyncRegistry registry,
            ObjectMapper mapper,
            @Value("${splitr.callback-url}") String url,
            @Value("${splitr.bus.kafka.query.topic:tr.kontas.splitr.query.topic}") String queryTopic,
            @Value("${splitr.bus.default-timeout:10000}") int defaultTimeout
        ) {
        return new KafkaQueryBus(queryTopic, kafka, registry, mapper, url, defaultTimeout);
    }

    @Bean
    public CommandCallbackController commandCallbackController(SyncRegistry registry) {
        return new CommandCallbackController(registry);
    }

    @Bean
    public CommandBus commandBus(
            KafkaTemplate<String, CommandRequest> kafka,
            SyncRegistry registry,
            ObjectMapper mapper,
            @Value("${splitr.callback-url}") String url,
            @Value("${splitr.bus.kafka.command.topic:tr.kontas.splitr.command.topic}") String queryTopic,
            @Value("${splitr.bus.default-timeout:10000}") int defaultTimeout
    ) {
        return new KafkaCommandBus(queryTopic, kafka, registry, mapper, url, defaultTimeout);
    }
}
