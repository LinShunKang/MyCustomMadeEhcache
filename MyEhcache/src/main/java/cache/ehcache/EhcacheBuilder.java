package cache.ehcache;

import cache.Cache;
import cache.CacheStats;
import cache.SimpleStatsCounter;
import cache.StatsCounter;
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
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.serialization.Serializer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LinShunkang on 7/15/17.
 */
public final class EhcacheBuilder<K, V> {

    private static final String DEFAULT_CACHE_NAME_PREFIX = "Ehcache-";

    private static final int DEFAULT_CACHE_SHARD_NUM = 1;

    private static final long DEFAULT_HEAP_SIZE = 0;

    private static final MemoryUnit DEFAULT_HEAP_MEM_UNIT = MemoryUnit.MB;

    private static final long DEFAULT_OFF_HEAP_SIZE = 128;

    private static final MemoryUnit DEFAULT_OFF_HEAP_MEM_UNIT = MemoryUnit.MB;

    private static final AtomicInteger DEFAULT_CACHE_NAME_SUFFIX = new AtomicInteger(0);

    private static final Expiry DEFAULT_TTL_EXPIRY = Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.HOURS));

    private static final Copier DEFAULT_KEY_COPIER = new KryoCopier();

    private static final Copier DEFAULT_VALUE_COPIER = new KryoCopier();

    private static final Serializer DEFAULT_KEY_SERIALIZER = new KryoSerializer();

    private static final Serializer DEFAULT_VALUE_SERIALIZER = new KryoSerializer();

    private static final StatsCounter DEFAULT_STATS_COUNTER = new StatsCounter() {
        @Override
        public void recordHits(int count) {
            //empty
        }

        @Override
        public void recordMisses(int count) {
            //empty
        }

        @Override
        public CacheStats getCacheStats() {
            return CacheStats.getInstance(0, 0);
        }
    };

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

    private String cacheName;

    private int shardNum = DEFAULT_CACHE_SHARD_NUM;

    private long heapSizePerShard = DEFAULT_HEAP_SIZE;

    private MemoryUnit heapMemoryUnit = DEFAULT_HEAP_MEM_UNIT;

    private long offHeapSizePerShard = DEFAULT_OFF_HEAP_SIZE;

    private MemoryUnit offHeapMemoryUnit = DEFAULT_OFF_HEAP_MEM_UNIT;

    private Class keyClass;

    private Class valueClass;

    private Expiry ttlExpiry = DEFAULT_TTL_EXPIRY;

    private Copier keyCopier = DEFAULT_KEY_COPIER;

    private Copier valueCopier = DEFAULT_VALUE_COPIER;

    private Serializer keySerializer = DEFAULT_KEY_SERIALIZER;

    private Serializer valueSerializer = DEFAULT_VALUE_SERIALIZER;

    private CacheEventListenerConfigurationBuilder cacheEventListenerConfigurationBuilder;

    private CacheLoaderWriter cacheLoaderWriter;

    private StatsCounter statsCounter = DEFAULT_STATS_COUNTER;

    private EhcacheBuilder(Class<K> keyClass, Class<V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    public static <K, V> EhcacheBuilder<K, V> newBuilder(Class<K> keyClass, Class<V> valueClass) {
        return new EhcacheBuilder<>(keyClass, valueClass);
    }

    public EhcacheBuilder<K, V> cacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public EhcacheBuilder<K, V> shardNum(int shardNum) {
        this.shardNum = getMinShardNum(shardNum);
        return this;
    }

    private int getMinShardNum(int shardNum) {
        int number = 1;
        while (number < shardNum) {
            number = number << 1;
        }
        return number;
    }

    public String getCacheName() {
        return cacheName != null ? cacheName : DEFAULT_CACHE_NAME_PREFIX + DEFAULT_CACHE_NAME_SUFFIX.getAndIncrement();
    }

    /**
     * 注意：实际上总的堆内内存大小为shardNum*heapPerShard
     *
     * @param heapSizePerShard 单个分片所占的堆内内存大小(MB)
     * @return
     */
    public EhcacheBuilder<K, V> heapPerShard(long heapSizePerShard, MemoryUnit memoryUnit) {
        if (heapSizePerShard < 0 || memoryUnit == null) {
            throw new IllegalArgumentException("heapPerShardSizeOfMB must >= 0 and memoryUnit must not null!!!");
        }

        this.heapSizePerShard = heapSizePerShard;
        this.heapMemoryUnit = memoryUnit;
        return this;
    }

    /**
     * 注意：实际上总的堆外内存大小为shardNum*heapSizePerShard
     *
     * @param offHeapSizePerShard 单个分片所占的堆外内存大小(MB)
     * @return
     */
    public EhcacheBuilder<K, V> offHeapPerShard(long offHeapSizePerShard, MemoryUnit memoryUnit) {
        if (offHeapSizePerShard < 0) {
            throw new IllegalArgumentException("offHeapSizePerShard must >= 0 and memoryUnit must not null!!!");
        }

        this.offHeapSizePerShard = offHeapSizePerShard;
        this.offHeapMemoryUnit = memoryUnit;
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

    public EhcacheBuilder<K, V> cacheEventListener(CacheEventListener<K, V> cacheEventListener, EventType eventType, EventType... eventTypes) {
        this.cacheEventListenerConfigurationBuilder = CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(
                cacheEventListener,
                eventType,
                eventTypes)
                .unordered()
                .asynchronous();
        return this;
    }

    public EhcacheBuilder<K, V> recordStats() {
        statsCounter = SimpleStatsCounter.getInstance();
        return this;
    }

    public EhcacheBuilder<K, V> loaderWriter(CacheLoaderWriter<K, V> cacheLoaderWriter) {
        this.cacheLoaderWriter = cacheLoaderWriter;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
        checkParams();

        CacheConfigurationBuilder<K1, V1> configurationBuilder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, valueClass, getResourcePoolsBuilder())
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

        if (cacheLoaderWriter != null) {
            configurationBuilder = configurationBuilder.withLoaderWriter(cacheLoaderWriter);
        }

        CacheConfiguration<K1, V1> cacheConfiguration = configurationBuilder.build();
        String cacheName = getCacheName();
        org.ehcache.Cache<K1, V1>[] caches = new org.ehcache.Cache[shardNum];
        for (int i = 0; i < shardNum; ++i) {
            String name = cacheName + "_" + i;
            caches[i] = cacheManager.createCache(name, cacheConfiguration);
            System.out.println("cache: " + name + " created");
        }
        return new ShardEhcache<>(caches, cacheName, statsCounter);
    }

    private void checkParams() {
        if (heapSizePerShard <= 0L && offHeapSizePerShard <= 0L) {
            throw new IllegalArgumentException("heapSizePerShard and offHeapSizePerShard must have one great than 0!!!");
        }
    }

    private ResourcePoolsBuilder getResourcePoolsBuilder() {
        ResourcePoolsBuilder builder = ResourcePoolsBuilder.newResourcePoolsBuilder();
        if (heapSizePerShard > 0L) {
            builder = builder.heap(heapSizePerShard, heapMemoryUnit);
        }

        return builder.offheap(offHeapSizePerShard, offHeapMemoryUnit);
    }
}
