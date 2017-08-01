package cache.guava;

import cache.Cache;
import cache.CacheStats;
import com.google.common.cache.CacheBuilder;
import kryo.KryoUtil;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by thinker on 7/21/17.
 */

/**
 * 暂不推荐使用该类，因为使用该类会导致内存泄露，
 * 因为当有大量的ByteBuffer存在于老年代、堆外内存使用殆尽并且老年代没有进行垃圾收集时会导致ByteBuffer.allocateDirect()无法分配更多的内存，抛出OutOfMemoryError异常
 */
public class GuavaOffHeapCache<K, V> implements Cache<K, V> {

    private com.google.common.cache.Cache<K, ByteBuffer> byteBufferCache = CacheBuilder.newBuilder().weakKeys().weakValues().concurrencyLevel(128).expireAfterWrite(1, TimeUnit.HOURS).recordStats().maximumSize(1500000).build();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void put(K key, V value) {
        byte[] bytes = KryoUtil.writeToByteArrayOpt(value);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
        byteBufferCache.put(key, byteBuffer);
    }

    @Override
    public V get(K key) {
        ByteBuffer byteBuffer = byteBufferCache.getIfPresent(key);
        if (byteBuffer != null) {
            byteBuffer = byteBuffer.duplicate();
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            return KryoUtil.readFromByteArrayOpt(bytes);
        }
        return null;
    }

    @Override
    public void remove(K key) {
        byteBufferCache.invalidate(key);
    }

    @Override
    public Map<K, V> getAll(Set<? extends K> keys) {
        return null;
    }

    @Override
    public void removeAll(Set<? extends K> keys) {
        byteBufferCache.invalidateAll(keys);
    }

    @Override
    public void clear() {
        byteBufferCache.invalidateAll();
    }


    @Override
    public void putAll(Map<? extends K, ? extends V> entries) {

    }

    @Override
    public CacheStats stats() {
        return null;
    }
}
