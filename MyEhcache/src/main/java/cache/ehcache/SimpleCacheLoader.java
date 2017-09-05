package cache.ehcache;

import cache.StatsCounter;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import java.util.Map;


/**
 * Created by LinShunkang on 9/5/17.
 */
public class SimpleCacheLoader<K, V> implements CacheLoaderWriter<K, V> {

    private final CacheLoaderWriter<K, V> cacheLoaderWriter;

    private final StatsCounter statsCounter;

    private SimpleCacheLoader(CacheLoaderWriter<K, V> cacheLoaderWriter, StatsCounter statsCounter) {
        this.cacheLoaderWriter = cacheLoaderWriter;
        this.statsCounter = statsCounter;
    }

    @Override
    public V load(K key) throws Exception {
        statsCounter.recordFakeHits(1);
        return cacheLoaderWriter.load(key);
    }

    @Override
    public Map<K, V> loadAll(Iterable<? extends K> keys) throws BulkCacheLoadingException, Exception {
        return cacheLoaderWriter.loadAll(keys);
    }

    @Override
    public void write(K key, V value) throws Exception {
        cacheLoaderWriter.write(key, value);
    }

    @Override
    public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws BulkCacheWritingException, Exception {
        cacheLoaderWriter.writeAll(entries);
    }

    @Override
    public void delete(K key) throws Exception {
        cacheLoaderWriter.delete(key);
    }

    @Override
    public void deleteAll(Iterable<? extends K> keys) throws BulkCacheWritingException, Exception {
        cacheLoaderWriter.deleteAll(keys);
    }

    public CacheLoaderWriter<K, V> getCacheLoaderWriter() {
        return cacheLoaderWriter;
    }

    public StatsCounter getStatsCounter() {
        return statsCounter;
    }

    public static <K, V> SimpleCacheLoader<K, V> getInstance(CacheLoaderWriter<K, V> cacheLoaderWriter, StatsCounter statsCounter) {
        return new SimpleCacheLoader<>(cacheLoaderWriter, statsCounter);
    }
}
