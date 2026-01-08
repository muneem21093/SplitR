package tr.kontas.splitr.consumer.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tr.kontas.splitr.bus.command.CommandBus;
import tr.kontas.splitr.bus.event.EventBus;
import tr.kontas.splitr.bus.query.QueryBus;
import tr.kontas.splitr.bus.registry.SyncRegistry;
import tr.kontas.splitr.consumer.bus.CommandHandler;
import tr.kontas.splitr.consumer.bus.EventHandler;
import tr.kontas.splitr.consumer.bus.QueryHandler;
import tr.kontas.splitr.consumer.bus.impl.InMemoryCommandBus;
import tr.kontas.splitr.consumer.bus.impl.InMemoryEventBus;
import tr.kontas.splitr.consumer.bus.impl.InMemoryQueryBus;
import tr.kontas.splitr.consumer.dispatcher.CommandDispatcher;
import tr.kontas.splitr.consumer.dispatcher.EventDispatcher;
import tr.kontas.splitr.consumer.dispatcher.QueryDispatcher;
import tr.kontas.splitr.consumer.store.IdempotencyStore;
import tr.kontas.splitr.consumer.store.LruStore;

import java.util.List;

@Configuration
public class InMemoryBusAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public SyncRegistry syncRegistry() {
        return new SyncRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public LruStore lruStore(@Value("${splitr.idempotency.max-size:100}") int max) {
        return new LruStore(max);
    }

    @Bean
    public CommandDispatcher commandDispatcher(List<CommandHandler<?>> handlers,
                                               LruStore store,
                                               ObjectMapper mapper) {
        return new CommandDispatcher(handlers, store, mapper);
    }

    @Bean
    public QueryDispatcher queryDispatcher(List<QueryHandler<?>> handlers,
                                           LruStore store,
                                           ObjectMapper mapper) {
        return new QueryDispatcher(handlers, store, mapper);
    }

    @Bean
    public EventDispatcher eventDispatcher(List<EventHandler<?>> handlers,
                                           LruStore store,
                                           ObjectMapper mapper) {
        return new EventDispatcher(handlers, store, mapper);
    }

    @Bean
    public CommandBus commandBus(CommandDispatcher dispatcher, ObjectMapper mapper, IdempotencyStore store) {
        return new InMemoryCommandBus(dispatcher, mapper, store);
    }

    @Bean
    public QueryBus queryBus(QueryDispatcher dispatcher, ObjectMapper mapper, IdempotencyStore store) {
        return new InMemoryQueryBus(dispatcher, mapper, store);
    }

    @Bean
    public EventBus eventBus(EventDispatcher dispatcher, ObjectMapper mapper) {
        return new InMemoryEventBus(dispatcher, mapper);
    }
}
