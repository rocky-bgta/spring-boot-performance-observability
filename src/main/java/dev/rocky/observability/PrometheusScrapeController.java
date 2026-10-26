package dev.rocky.observability;

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Explicit scrape route for the observability example.
 * Delegates to the actual Micrometer Prometheus registry, not mock metrics.
 */
@RestController
public class PrometheusScrapeController {
    private final PrometheusMeterRegistry prometheus;

    public PrometheusScrapeController(PrometheusMeterRegistry prometheus) {
        this.prometheus = prometheus;
    }

    @GetMapping(value = "/actuator/prometheus", produces = MediaType.TEXT_PLAIN_VALUE)
    public String scrape() {
        return prometheus.scrape();
    }
}
