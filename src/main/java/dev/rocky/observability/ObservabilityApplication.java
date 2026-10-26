package dev.rocky.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ObservabilityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ObservabilityApplication.class, args);
    }

    @RestController
    static class WorkController {
        private final MeterRegistry registry;

        WorkController(MeterRegistry registry) {
            this.registry = registry;
        }

        @GetMapping("/work")
        public String work() {
            long start = System.nanoTime();
            try {
                return "ok";
            } finally {
                Timer.builder("demo.work.latency")
                    .description("Duration of demo work requests")
                    .publishPercentileHistogram()
                    .register(registry)
                    .record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            }
        }
    }
}
