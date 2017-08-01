package cache;


public interface StatsCounter {

    void recordHits(int count);

    void recordMisses(int count);

    CacheStats getCacheStats();

}
