package tr.kontas.splitr.test;

import tr.kontas.splitr.bus.command.Command;

public record CreateOrderCommand(String productName, int quantity) implements Command {
}
