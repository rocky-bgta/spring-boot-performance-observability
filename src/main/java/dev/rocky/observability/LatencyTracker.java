package dev.rocky.observability;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/** Basic concurrent latency counters; use Micrometer histograms for production percentiles. */
public final class LatencyTracker {
    private final LongAdder requests = new LongAdder();
    private final LongAdder totalNanos = new LongAdder();
    private final LongAccumulator maxNanos = new LongAccumulator(Long::max, 0);

    public void record(long elapsedNanos) {
        if (elapsedNanos < 0) throw new IllegalArgumentException("negative duration");
        totalNanos.add(elapsedNanos);
        maxNanos.accumulate(elapsedNanos);
        requests.increment();
    }

    public long requests() { return requests.sum(); }
    public double averageNanos() { long n = requests(); return n == 0 ? 0 : (double) totalNanos.sum() / n; }
    public long maximumNanos() { return maxNanos.get(); }
}
