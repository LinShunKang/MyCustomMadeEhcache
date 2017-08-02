package cache;

import org.terracotta.statistics.jsr166e.LongAdder;

public class SimpleStatsCounter implements StatsCounter {

    private LongAdder hitCount = new LongAdder();

    private LongAdder missCount = new LongAdder();

    private SimpleStatsCounter() {
    }

    public void recordHits(int count) {
        hitCount.add(count);
    }

    public void recordMisses(int count) {
        missCount.add(count);
    }

    public CacheStats getCacheStats() {
        return CacheStats.getInstance(hitCount.sum(), missCount.sum());
    }

    public static SimpleStatsCounter getInstance() {
        return new SimpleStatsCounter();
    }
}
