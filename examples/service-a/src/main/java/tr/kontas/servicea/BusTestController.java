package tr.kontas.servicea;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.kontas.splitr.bus.CommandBus;
import tr.kontas.splitr.bus.QueryBus;
import tr.kontas.splitr.test.CreateOrderCommand;
import tr.kontas.splitr.test.OrderQuery;

@RestController
@RequestMapping("/test-bus")
@RequiredArgsConstructor
@Slf4j
public class BusTestController {

    private final CommandBus commandBus;
    private final QueryBus queryBus;


    @GetMapping("/query-sync")
    public String querySync() {
        var query = new OrderQuery("ORD-100");
        return queryBus.publishSync(query, String.class);
    }

    @GetMapping("/query-async")
    public String queryAsync() {
        var query = new OrderQuery("ORD-100");
        queryBus.publishAsync(query, String.class)
                .thenAccept(res -> System.out.println("Async Query Result: " + res));
        return "Async query triggered";
    }

    @GetMapping("/command-sync-timeout")
    public String commandSyncWithTimeout() {
        var command = new CreateOrderCommand("Laptop", 1);
        return commandBus.publishSync(command, String.class, 10);
    }

    @GetMapping("/command-sync-default")
    public String commandSyncDefault() {
        var command = new CreateOrderCommand("Laptop", 1);
        return commandBus.publishSync(command, String.class);
    }

    @GetMapping("/command-async")
    public String commandAsync() {
        var command = new CreateOrderCommand("Laptop", 1);
        commandBus.publishAsync(command, String.class)
                .thenAccept(res -> System.out.println("Unused Async Command Result: " + res));
        return "Unused async command triggered";
    }

    @GetMapping("/command-fire")
    public String commandFire() {
        var command = new CreateOrderCommand("Laptop", 1);
        commandBus.publish(command);
        return "Fire-and-forget command triggered";
    }

    @GetMapping("/command-fire-timeout")
    public String commandFireWithTimeout() {
        var command = new CreateOrderCommand("Laptop", 1);
        commandBus.publish(command, 10);
        return "Fire-and-forget with timeout triggered";
    }
}

