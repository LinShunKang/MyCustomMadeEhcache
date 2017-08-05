package cache.ehcache;

import cache.Cache;
import cache.CacheStats;
import cache.StatsCounter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 该类通过对ehcache的简单封装，实现了以下特性：
 * 1、通过支持分片，提升并发性能
 * 2、通过StatsCounter，支持缓存命中计数
 */
public class ShardEhcache<K, V> implements Cache<K, V> {

    private final org.ehcache.Cache<K, V>[] caches;

    private final int mask;

    private final String name;

    private final StatsCounter statsCounter;

    public ShardEhcache(org.ehcache.Cache<K, V>[] caches, String name, StatsCounter statsCounter) {
        if (caches == null || caches.length <= 0 || !isPowerOfTwo(caches.length)) {
            throw new IllegalArgumentException("caches.length must be a power of two!!!");
        }

        this.caches = caches;
        this.mask = caches.length - 1;
        this.name = name;
        this.statsCounter = statsCounter;
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
    public String getName() {
        return name;
    }

    @Override
    public V get(K key) {
        org.ehcache.Cache<K, V> cache = getCache(key);
        V result = cache.get(key);
        record(result);
        return result;
    }

    private void record(V value) {
        if (value != null) {
            statsCounter.recordHits(1);
        } else {
            statsCounter.recordMisses(1);
        }
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
        Map<Integer, Set<K>> shardKeySetMap = new HashMap<>((int) (caches.length / .75) + 1);
        for (K key : keys) {
            int shard = hash(key) & mask;
            Set<K> shardKeySet = shardKeySetMap.get(shard);
            if (shardKeySet == null) {
                shardKeySet = new HashSet<>((int) ((keys.size() / caches.length) / .75) + 1);
                shardKeySetMap.put(shard, shardKeySet);
            }
            shardKeySet.add(key);
        }

        Map<K, V> result = new HashMap<>((int) (keys.size() / .75) + 1);
        for (Map.Entry<Integer, Set<K>> entry : shardKeySetMap.entrySet()) {
            Integer index = entry.getKey();
            Set<K> shardKeySet = entry.getValue();
            result.putAll(caches[index].getAll(shardKeySet));
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
