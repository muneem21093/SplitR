package tr.kontas.splitr.bus.command;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.kontas.splitr.bus.registry.SyncRegistry;
import tr.kontas.splitr.dto.CommandResponse;

@RestController
@RequestMapping("/internal/command")
public class CommandCallbackController {

    private final SyncRegistry registry;
    public CommandCallbackController(SyncRegistry registry) { this.registry = registry; }

    @PostMapping("/callback")
    public void callback(@RequestBody CommandResponse r) {
        registry.complete(r);
    }
}
