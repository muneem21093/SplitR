package tr.kontas.serviceb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.splitr.consumer.CommandHandler;
import tr.kontas.splitr.test.CreateOrderCommand;

@Component
@Slf4j
public class OrderCommandHandler implements CommandHandler<CreateOrderCommand> {
    @Override public Class<CreateOrderCommand> type() { return CreateOrderCommand.class; }

    @Override
    public Object handle(CreateOrderCommand cmd) {
        log.atInfo().log("Command executed for: %s %d".formatted(cmd.productName(), cmd.quantity()));
        return "SUCCESS";
    }
}
