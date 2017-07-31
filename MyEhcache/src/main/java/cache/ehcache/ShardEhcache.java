package cache.ehcache;

import cache.Cache;
import cache.CacheStats;
import cache.StatsCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 这个类是用来弥补ehcache本身不支持设置并发度而创建的，
 * get()方法在多线程下可以降低将近19%的响应时间。
 */
public class ShardEhcache<K, V> implements Cache<K, V> {

    private org.ehcache.Cache<K, V>[] caches;

    private int mask;

    private StatsCounter statsCounter;

    public ShardEhcache(org.ehcache.Cache<K, V>[] caches) {
        if (caches == null || caches.length <= 0 || !isPowerOfTwo(caches.length)) {
            throw new IllegalArgumentException("caches.length must be a power of two!!!");
        }

        this.caches = caches;
        this.mask = caches.length - 1;
        this.statsCounter = StatsCounter.getInstance();
    }

    private boolean isPowerOfTwo(int num) {
        return num > 0 && (num & num - 1) == 0;
    }

    private int hash(K key) {
        return key.hashCode();
    }

    private org.ehcache.Cache<K, V> getCache(K key) {
        int cacheIndex = hash(key) & mask;
        return caches[cacheIndex];
    }

    @Override
    public V get(K key) {
        org.ehcache.Cache<K, V> cache = getCache(key);
        V result = cache.get(key);
        if (result != null) {
            statsCounter.recordHits(1);
        } else {
            statsCounter.recordMisses(1);
        }
        return result;
    }

    @Override
    public void put(K key, V value) {
        org.ehcache.Cache<K, V> cache = getCache(key);
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        org.ehcache.Cache<K, V> cache = getCache(key);
        cache.remove(key);
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        Map<K, V> result = new HashMap<>((int) (keys.size() / .75) + 1);
        for (K key : keys) {
            V value = get(key);
            if (value != null) {
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> entries) {
        for (Map.Entry<? extends K, ? extends V> entry : entries.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        for (int i = 0; i < caches.length; ++i) {
            caches[i].removeAll(keys);
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < caches.length; ++i) {
            caches[i].clear();
        }
    }

    @Override
    public CacheStats stats() {
        return statsCounter.getCacheStats();
    }
}
