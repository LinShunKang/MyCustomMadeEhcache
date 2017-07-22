package ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.serialization.Serializer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LinShunkang on 7/15/17.
 */
public final class EhcacheBuilder<K, V> {

    private static final String DEFAULT_CACHE_NAME_PREFIX = "Ehcache_";

    private static final AtomicInteger DEFAULT_CACHE_NAME_SUFFIX = new AtomicInteger(0);

    private static final Expiry DEFAULT_TTL_EXPIRY = Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.HOURS));

    private static final Copier DEFAULT_KEY_COPIER = new KryoCopier();

    private static final Copier DEFAULT_VALUE_COPIER = new KryoCopier();

    private static final Serializer DEFAULT_KEY_SERIALIZER = new KryoSerializer();

    private static final Serializer DEFAULT_VALUE_SERIALIZER = new KryoSerializer();

    private static final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    cacheManager.close();
                } finally {
                    System.out.println("EhcacheBuilder: cacheManager.close()");
                }
            }
        });
    }

    private ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder();

    private String cacheName;

    private Class keyClass;

    private Class valueClass;

    private Expiry ttlExpiry = DEFAULT_TTL_EXPIRY;

    private Copier keyCopier = DEFAULT_KEY_COPIER;

    private Copier valueCopier = DEFAULT_VALUE_COPIER;

    private Serializer keySerializer = DEFAULT_KEY_SERIALIZER;

    private Serializer valueSerializer = DEFAULT_VALUE_SERIALIZER;

    private CacheEventListenerConfigurationBuilder cacheEventListenerConfigurationBuilder;

    private EhcacheBuilder(Class<K> keyClass, Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public static EhcacheBuilder<Object, Object> newBuilder(Class keyClass, Class valueClass) {
        return new EhcacheBuilder<>(keyClass, valueClass);
    }

    public EhcacheBuilder<K, V> cacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public String getCacheName() {
        return cacheName != null ? cacheName : DEFAULT_CACHE_NAME_PREFIX + DEFAULT_CACHE_NAME_SUFFIX.getAndIncrement();
    }

    public EhcacheBuilder<K, V> heap(long heapSize, MemoryUnit heapUnit) {
        if (heapSize > 0L) {
            resourcePoolsBuilder = resourcePoolsBuilder.heap(heapSize, heapUnit);
        }
        return this;
    }

    public EhcacheBuilder<K, V> offHeap(long offHeapSize, MemoryUnit offHeapUnit) {
        resourcePoolsBuilder = resourcePoolsBuilder.offheap(offHeapSize, offHeapUnit);
        return this;
    }

    @SuppressWarnings("unchecked")
    public EhcacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit durationUnit) {
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

    public EhcacheBuilder<K, V> compress(boolean compress) {
        if (compress) {
            keySerializer = new KryoCompressSerializer<>();
            valueSerializer = new KryoCompressSerializer<>();
        } else {
            keySerializer = new KryoSerializer<>();
            valueSerializer = new KryoSerializer<>();
        }
        return this;
    }

    public EhcacheBuilder<K, V> cacheEventListener(CacheEventListener<?, ?> cacheEventListener, EventType eventType, EventType... eventTypes) {
        this.cacheEventListenerConfigurationBuilder = CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(
                cacheEventListener,
                eventType,
                eventTypes)
                .unordered()
                .asynchronous();
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        CacheConfigurationBuilder configurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, valueClass, resourcePoolsBuilder)
                .withSizeOfMaxObjectGraph(100)
                .withSizeOfMaxObjectSize(50, MemoryUnit.KB)
                .withExpiry(ttlExpiry)
                .withKeySerializer(keySerializer)
                .withValueSerializer(valueSerializer)
                .withValueCopier(valueCopier)
                .withKeyCopier(keyCopier);

        if (cacheEventListenerConfigurationBuilder != null) {
            configurationBuilder = configurationBuilder.add(cacheEventListenerConfigurationBuilder);
        }

        CacheConfiguration<K1, V1> cacheConfiguration = configurationBuilder.build();
        return cacheManager.createCache(getCacheName(), cacheConfiguration);
    }

}
