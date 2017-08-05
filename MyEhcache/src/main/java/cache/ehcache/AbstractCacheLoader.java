package cache.ehcache;

import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import java.util.Map;

public abstract class AbstractCacheLoader<K, V> implements CacheLoaderWriter<K, V> {

    @Override
    public abstract V load(K key) throws Exception;

    @Override
    public abstract Map<K, V> loadAll(Iterable<? extends K> keys) throws BulkCacheLoadingException, Exception;

    @Override
    public void write(K key, V value) throws Exception {
        //empty
    }

    @Override
    public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws BulkCacheWritingException, Exception {
        //empty
    }

    @Override
    public void delete(K key) throws Exception {
        //empty
    }

    @Override
    public void deleteAll(Iterable<? extends K> keys) throws BulkCacheWritingException, Exception {
        //empty
    }
}
