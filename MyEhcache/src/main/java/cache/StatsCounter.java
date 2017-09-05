package cache;


public interface StatsCounter {

    void recordHits(int count);

    void recordFakeHits(int count);

    void recordMisses(int count);

    CacheStats getCacheStats();

}
