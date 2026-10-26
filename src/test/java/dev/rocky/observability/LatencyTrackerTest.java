package dev.rocky.observability;

import org.junit.jupiter.api.Test;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

class LatencyTrackerTest {
 @Test void emptyTrackerReturnsZeros(){var t=new LatencyTracker();assertEquals(0,t.requests());assertEquals(0,t.averageNanos());assertEquals(0,t.maximumNanos());}
 @Test void recordsConcurrentSamples() throws Exception {var t=new LatencyTracker();var pool=Executors.newFixedThreadPool(4);try{var tasks=new java.util.ArrayList<Future<?>>();for(int i=0;i<100;i++){final int n=i+1;tasks.add(pool.submit(()->t.record(n)));}for(var f:tasks)f.get(5,TimeUnit.SECONDS);assertEquals(100,t.requests());assertEquals(50.5,t.averageNanos());assertEquals(100,t.maximumNanos());}finally{pool.shutdownNow();}}
 @Test void rejectsNegativeDuration(){assertThrows(IllegalArgumentException.class,()->new LatencyTracker().record(-1));}
}
