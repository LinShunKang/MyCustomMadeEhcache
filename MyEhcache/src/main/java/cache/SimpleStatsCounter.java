package cache;

import org.terracotta.statistics.jsr166e.LongAdder;

public class SimpleStatsCounter implements StatsCounter {

    private LongAdder hitCount = new LongAdder();//命中次数计数器，总的命中，包括假的命中和真正的命中

    private LongAdder fakeHitCount = new LongAdder();//命中次数计数器，假的命中，是因为引入CacheLoaderWriter后ShardEhcache中每次调用cache.get()一定返回的是非null，这时ShardEhcache会认为hit了，其实是miss的

    private LongAdder missCount = new LongAdder();//未命中计数器，真正的未命中，不包含假的命中

    private SimpleStatsCounter() {
    }

    public void recordHits(int count) {
        hitCount.add(count);
    }

    @Override
    public void recordFakeHits(int count) {
        fakeHitCount.add(count);
    }

    public void recordMisses(int count) {
        missCount.add(count);
    }

    public CacheStats getCacheStats() {
        long fakeHitCountSum = fakeHitCount.sum();
        return CacheStats.getInstance(hitCount.sum() - fakeHitCountSum, missCount.sum() + fakeHitCountSum);
    }

    public static SimpleStatsCounter getInstance() {
        return new SimpleStatsCounter();
    }
}
