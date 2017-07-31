package cache;

import java.util.concurrent.atomic.AtomicLong;

public class StatsCounter {

    private AtomicLong hitCount = new AtomicLong(0L);

    private AtomicLong missCount = new AtomicLong(0L);

    private StatsCounter() {
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

    public static StatsCounter getInstance() {
        return new StatsCounter();
    }
}
