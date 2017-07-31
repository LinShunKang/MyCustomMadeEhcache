package cache;

public class CacheStats {

    private final long hitCount;

    private final long missCount;

    private CacheStats(long hitCount, long missCount) {
        this.hitCount = hitCount;
        this.missCount = missCount;
    }

    public long getHitCount() {
        return hitCount;
    }

    public long getMissCount() {
        return missCount;
    }

    public double getHitRate() {
        long requestCount = hitCount + missCount;
        return (requestCount == 0) ? 1.0D : (double) hitCount / requestCount;
    }

    public static CacheStats getInstance(long hitCount, long missCount) {
        return new CacheStats(hitCount, missCount);
    }

    @Override
    public String toString() {
        return "CacheStats{" +
                "hitCount=" + hitCount +
                ", missCount=" + missCount +
                ", hitRate=" + getHitRate() +
                '}';
    }
}
