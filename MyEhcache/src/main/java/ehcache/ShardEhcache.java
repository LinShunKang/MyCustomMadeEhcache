package ehcache;

import org.ehcache.Cache;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 这个类是用来弥补ehcache本身不支持设置并发度而创建的，
 * get()方法在多线程下可以降低将近19%的响应时间。
 */
public class ShardEhcache<K, V> implements Cache<K, V> {

    private Cache<K, V>[] caches;

    private int mask;

    public ShardEhcache(Cache<K, V>[] caches) {
        if (caches == null || caches.length <= 0 || !isPowerOfTwo(caches.length)) {
            throw new IllegalArgumentException("caches.length must be a power of two!!!");
        }

        this.caches = caches;
        this.mask = caches.length - 1;
    }

    private boolean isPowerOfTwo(int num) {
        return num > 0 && (num & num - 1) == 0;
    }

    private int hash(K key) {
        return key.hashCode();
    }

    private Cache<K, V> getCache(K key) {
        int cacheIndex = hash(key) & mask;
        return caches[cacheIndex];
    }

    @Override
    public V get(K key) throws CacheLoadingException {
        Cache<K, V> cache = getCache(key);
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) throws CacheWritingException {
        Cache<K, V> cache = getCache(key);
        cache.put(key, value);
    }

    @Override
    public boolean containsKey(K key) {
        Cache<K, V> cache = getCache(key);
        return cache.containsKey(key);
    }

    @Override
    public void remove(K key) throws CacheWritingException {
        Cache<K, V> cache = getCache(key);
        cache.remove(key);
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) throws BulkCacheLoadingException {
        throw new UnsupportedOperationException("Sorry, this is operation not supporter yet!!!");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> entries) throws BulkCacheWritingException {
        throw new UnsupportedOperationException("Sorry, this is operation not supporter yet!!!");
    }

    @Override
    public void removeAll(Set<? extends K> keys) throws BulkCacheWritingException {
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
    public V putIfAbsent(K key, V value) throws CacheLoadingException, CacheWritingException {
        Cache<K, V> cache = getCache(key);
        return cache.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(K key, V value) throws CacheWritingException {
        Cache<K, V> cache = getCache(key);
        return cache.remove(key, value);
    }

    @Override
    public V replace(K key, V value) throws CacheLoadingException, CacheWritingException {
        Cache<K, V> cache = getCache(key);
        return cache.replace(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) throws CacheLoadingException, CacheWritingException {
        Cache<K, V> cache = getCache(key);
        return cache.replace(key, oldValue, newValue);
    }

    @Override
    public CacheRuntimeConfiguration<K, V> getRuntimeConfiguration() {
        return caches[0].getRuntimeConfiguration();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        throw new UnsupportedOperationException("Sorry, this is operation not supporter yet!!!");
    }
}
