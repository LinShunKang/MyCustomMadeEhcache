package cache;

import java.util.concurrent.atomic.AtomicLong;

public class SimpleStatsCounter implements StatsCounter {

    private AtomicLong hitCount = new AtomicLong(0L);

    private AtomicLong missCount = new AtomicLong(0L);

    private SimpleStatsCounter() {
    }

    public void recordHits(int count) {
        hitCount.addAndGet(count);
    }

    public void recordMisses(int count) {
        missCount.addAndGet(count);
    }

    public CacheStats getCacheStats() {
        return CacheStats.getInstance(hitCount.get(), missCount.get());
    }

    public static SimpleStatsCounter getInstance() {
        return new SimpleStatsCounter();
    }
}
