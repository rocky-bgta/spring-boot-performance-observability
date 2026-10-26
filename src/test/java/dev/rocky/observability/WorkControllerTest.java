package dev.rocky.observability;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WorkControllerTest {
    @Test void recordsRequestTimer() {
        try (SimpleMeterRegistry registry = new SimpleMeterRegistry()) {
            var controller = new ObservabilityApplication.WorkController(registry);
            assertEquals("ok", controller.work());
            assertEquals(1, registry.get("demo.work.latency").timer().count());
        }
    }
}
