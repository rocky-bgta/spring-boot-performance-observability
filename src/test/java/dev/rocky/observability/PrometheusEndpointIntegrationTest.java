package dev.rocky.observability;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrometheusEndpointIntegrationTest {
    @Autowired TestRestTemplate http;
    @LocalServerPort int port;

    @Test
    void scrapeIncludesDemoTimerAndHttpMetrics() {
        String base = "http://localhost:" + port;
        assertEquals("ok", http.getForObject(base + "/work", String.class));
        ResponseEntity<String> response =
            http.getForEntity(base + "/actuator/prometheus", String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), "Prometheus scrape returned " + response.getStatusCode() + ": " + response.getBody());
        String metrics = response.getBody();
        assertNotNull(metrics);
        assertTrue(metrics.contains("demo_work_latency_seconds_count"), metrics);
        assertTrue(metrics.contains("http_server_requests_seconds_count"), metrics);
    }
}
