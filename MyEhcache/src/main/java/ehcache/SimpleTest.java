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

import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/7/17.
 */
public class SimpleTest {

    public static void main(String[] args) {
        test1();
//        testOffHeap();
        testHeap();
    }

    public static void test1() {

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build())
                .build(true);

        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(100)).build());

        myCache.put(1L, "da one!");
        String value = myCache.get(1L);
        System.out.println(value);

        cacheManager.close();
    }

    @SuppressWarnings("unchecked")
    private static void testOffHeap() {
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(5, MemoryUnit.GB);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        Expiry expiry = Expirations.timeToLiveExpiration(Duration.of(5L, TimeUnit.SECONDS));
        CacheConfiguration cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, byte[].class, resourcePoolsBuilder).withExpiry(expiry).build();
        Cache<Long, byte[]> GPECache = cacheManager.createCache("GPE", cacheConfiguration);


        long starTime = System.currentTimeMillis();
        for (long i = 0; i < Integer.MAX_VALUE / 100; ++i) {
            GPECache.put(i, String.valueOf(i).getBytes());
        }

        for (long i = 0; i < Integer.MAX_VALUE/ 100; ++i) {
            GPECache.get(i);
        }
        System.out.println(System.currentTimeMillis() - starTime);
    }

    private static void testHeap() {
        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(5, MemoryUnit.GB);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
        Cache<Long, String> GPECache = cacheManager.createCache("GPE", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, resourcePoolsBuilder).build());
        System.out.println(GPECache.get(1L));


        long starTime = System.currentTimeMillis();
        for (long i = 0; i < Integer.MAX_VALUE / 100; ++i) {
            GPECache.put(i, String.valueOf(i));
        }

        for (long i = 0; i < Integer.MAX_VALUE/ 100; ++i) {
            GPECache.get(i);
        }
        System.out.println(System.currentTimeMillis() - starTime);
    }
}
