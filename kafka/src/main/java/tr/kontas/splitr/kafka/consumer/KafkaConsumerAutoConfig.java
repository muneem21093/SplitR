package tr.kontas.splitr.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import tr.kontas.splitr.consumer.*;

import java.util.List;

@Configuration
@EnableKafka
@Slf4j
@ConditionalOnProperty(name = "splitr.consumer.enabled", havingValue = "true")
public class KafkaConsumerAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    IdempotencyStore store(@Value("${splitr.idempotency.max-size:100}") int max) {
        return new LruStore(max);
    }

    @Bean
    @ConditionalOnMissingBean
    public QueryKafkaListener queryKafkaListener(QueryDispatcher dispatcher) {
        return new QueryKafkaListener(dispatcher);
    }

    @Bean
    public QueryDispatcher queryDispatcher(
            List<QueryHandler<?>> handlers,
            IdempotencyStore store,
            ObjectMapper mapper) {
        log.atInfo().log("Initializing QueryDispatcher");
        return new QueryDispatcher(handlers, store, mapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandKafkaListener commandKafkaListener(CommandDispatcher dispatcher) {
        return new CommandKafkaListener(dispatcher);
    }

    @Bean
    public CommandDispatcher commandDispatcher(
            List<CommandHandler<?>> handlers,
            IdempotencyStore store,
            ObjectMapper mapper) {
        log.atInfo().log("Initializing CommandDispatcher");
        return new CommandDispatcher(handlers, store, mapper);
    }
}