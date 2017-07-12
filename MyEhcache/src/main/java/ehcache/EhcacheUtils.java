package ehcache;

import model.WorkExperience;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;

import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/12/17.
 */
public final class EhcacheUtils {

    @SuppressWarnings("unchecked")
    public static <K, V> Expiry<K, V> getTTLExpiry(long length, TimeUnit timeUnit) {
        return (Expiry<K, V>) Expirations.timeToLiveExpiration(Duration.of(length, timeUnit));
    }

    public static ResourcePoolsBuilder getResourcePoolBuilder(long heapMB, long offHeapMB) {
        if (heapMB <= 0) {
            return ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(offHeapMB, MemoryUnit.MB);
        }
        return ResourcePoolsBuilder.newResourcePoolsBuilder().heap(heapMB, MemoryUnit.MB).offheap(offHeapMB, MemoryUnit.MB);
    }

    public static <K, V> CacheConfiguration<K, V> getCacheConf(Class<K> keyClass, Class<V> valueClass, long heapMB, long offHeapMB) {
        ResourcePoolsBuilder builder = EhcacheUtils.getResourcePoolBuilder(heapMB, offHeapMB);
        return CacheConfigurationBuilder.newCacheConfigurationBuilder(keyClass, valueClass, builder)
                .withSizeOfMaxObjectGraph(1000)
                .withSizeOfMaxObjectSize(20, MemoryUnit.KB)
                .withExpiry(EhcacheUtils.getTTLExpiry(5, TimeUnit.MINUTES))
                .withKeySerializer(new BZPCompressSerializer<>())
                .withValueSerializer(new BZPCompressSerializer<>())
                .withValueCopier(new BZPCopier<>())
//            .withKeyCopier(new BZPCopier<>())
//            .add(cacheEventListenerConfBuilder)
                .build();
    }

}
