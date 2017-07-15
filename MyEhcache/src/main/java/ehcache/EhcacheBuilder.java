package ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.serialization.Serializer;

import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/15/17.
 */
public final class EhcacheBuilder<K, V> {

    private ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder();

    private Class<K> keyClass;

    private Class<V> valueClass;

    private Expiry<? super K, ? super V> ttlExpiry;

    private Copier<K> keyCopier;

    private Copier<V> valueCopier;

    private Serializer<K> keySerializer;

    private Serializer<V> valueSerializer;

    private EhcacheBuilder(Class<K> keyClass, Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    private EhcacheBuilder() {
    }

    public static EhcacheBuilder<Object, Object> newBuilder(Class keyClass, Class valueClass) {
        return new EhcacheBuilder<>(keyClass, valueClass);
    }

    public static EhcacheBuilder<Object, Object> newBuilder() {
        return new EhcacheBuilder<>();
    }

    public EhcacheBuilder<K, V> heap(long heapSize, MemoryUnit heapUnit) {
        resourcePoolsBuilder.heap(heapSize, heapUnit);
        return this;
    }

    public EhcacheBuilder<K, V> offHeap(long offHeapSize, MemoryUnit offHeapUnit) {
        resourcePoolsBuilder.offheap(offHeapSize, offHeapUnit);
        return this;
    }

    @SuppressWarnings("unchecked")
    public EhcacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit durationUnit) {
        this.ttlExpiry = Expirations.timeToLiveExpiration(Duration.of(duration, durationUnit));
        return this;
    }

    public EhcacheBuilder<K, V> keyCopier(Copier<K> keyCopier) {
        this.keyCopier = keyCopier;
        return this;
    }

    public EhcacheBuilder<K, V> valueCopier(Copier<V> valueCopier) {
        this.valueCopier = valueCopier;
        return this;
    }

    public EhcacheBuilder<K, V> keySerializer(Serializer<K> keySerializer) {
        this.keySerializer = keySerializer;
        return this;
    }

    public EhcacheBuilder<K, V> valueSerializer(Serializer<V> valueSerializer) {
        this.valueSerializer = valueSerializer;
        return this;
    }

    public EhcacheBuilder compress(boolean compress) {
        if (compress) {
            keySerializer = new BZPCompressSerializer<>();
            valueSerializer = new BZPCompressSerializer<>();
        } else {
            keySerializer = new BZPSerializer<>();
            valueSerializer = new BZPSerializer<>();
        }
        return this;
    }

    //        public <K1 extends K, V1 extends V> Cache<K, V> build(Class<K> keyClass, Class<V> valueClass) {
    public <K1 extends K, V1 extends V> Cache<K, V> build() {
        CacheConfiguration<K, V> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, valueClass, resourcePoolsBuilder)
                .withSizeOfMaxObjectGraph(1000)
                .withSizeOfMaxObjectSize(20, MemoryUnit.KB)
                .withExpiry(ttlExpiry)
                .withKeySerializer(keySerializer)
                .withValueSerializer(valueSerializer)
                .withValueCopier(valueCopier)
                .withKeyCopier(keyCopier)
                .build();
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        return cacheManager.createCache("GeekWorkExp", cacheConfiguration);
    }
}
