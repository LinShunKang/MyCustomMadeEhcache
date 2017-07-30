package MyCustomMadeEhcache.ehcache;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/7/17.
 */
public class HelloEhcache {

    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
    }


    public static void test1() {
        ResourcePoolsBuilder builder = ResourcePoolsBuilder
//                .heap(64, MemoryUnit.MB)
                .heap(99)
//                .offheap(256, MemoryUnit.MB);
                ;

        CacheConfiguration<Long, String> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, builder)
                .withSizeOfMaxObjectGraph(1000)
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(5, TimeUnit.MINUTES)))
                .build();
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("myCache", cacheConfiguration)
                .build(true);
//        cacheManager.init();
        Cache<Long, String> cache = cacheManager.getCache("myCache", Long.class, String.class);

        for (long i = 0L; i < 120L; ++i) {
            cache.put(i, String.valueOf(i));
        }

        for (long i = 120L; i >= 0; --i) {
            System.out.println(i + ":" + cache.get(i));
//            sleep(50);
        }

        for (long i = 0L; i < 1000L; ++i) {
            System.out.println(i + ":" + cache.get(i));
//            sleep(50);
        }
    }

    private static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    private static void test2() {
        CacheEventListener cacheEventListener = new CacheEventListener<Long, String>() {
            public void onEvent(CacheEvent<? extends Long, ? extends String> cacheEvent) {
                System.out.println("CacheEventListener: " + cacheEvent.getType() + ", " + cacheEvent.getKey() + ", " + cacheEvent.getOldValue() + ", " + cacheEvent.getNewValue());
            }
        };
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(cacheEventListener, EventType.CREATED, EventType.UPDATED, EventType.EVICTED, EventType.REMOVED)
                .unordered().asynchronous();

        ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.heap(99).offheap(256, MemoryUnit.MB);

        final CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("foo", CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, resourcePoolsBuilder).add(cacheEventListenerConfiguration)
                ).build(true);

        final Cache<Long, String> cache = manager.getCache("foo", Long.class, String.class);
//        cache.put("Hello", "World");
//        cache.put("Hello", "Everyone");
//        cache.put("Hello2", "Everyone2");
//        cache.put("Hello3", "Everyone3");
//        cache.remove("Hello");
        for (long i = 0L; i < 120L; ++i) {
            cache.put(i, String.valueOf(i));
        }

        for (long i = 120L; i >= 0; --i) {
            System.out.println(i + ":" + cache.get(i));
//            sleep(50);
        }

        for (long i = 0L; i < 1000L; ++i) {
            System.out.println(i + ":" + cache.get(i));
//            sleep(50);
        }
    }

    //withLoaderWriter
    private static void test3() {
        ResourcePoolsBuilder builder = ResourcePoolsBuilder.heap(99).offheap(256, MemoryUnit.MB);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        Cache<Long, String> writeThroughCache = cacheManager.createCache("writeThroughCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class, builder)
                        .withLoaderWriter(new CacheLoaderWriter<Long, String>() {
                            public String load(Long aLong) throws Exception {
                                System.out.println("CacheLoaderWriter.load(" + aLong + ")");
                                return String.valueOf(aLong);
//                                return null;
                            }

                            public Map<Long, String> loadAll(Iterable<? extends Long> iterable) throws BulkCacheLoadingException, Exception {
                                System.out.println("CacheLoaderWriter.loadAll(" + iterable + ")");
                                Map<Long, String> result = new HashMap<>();
                                for (Long id : iterable) {
                                    result.put(id, load(id));
                                }
                                return result;
                            }

                            public void write(Long aLong, String s) throws Exception {
                                System.out.println("CacheLoaderWriter.write(" + aLong + ", " + s + ")");
                            }

                            public void writeAll(Iterable<? extends Map.Entry<? extends Long, ? extends String>> iterable) throws BulkCacheWritingException, Exception {
                                System.out.println("CacheLoaderWriter.writeAll(" + iterable + ")");
                            }

                            public void delete(Long aLong) throws Exception {
                                System.out.println("CacheLoaderWriter.delete(" + aLong + ")");
                            }

                            public void deleteAll(Iterable<? extends Long> iterable) throws BulkCacheWritingException, Exception {
                                System.out.println("CacheLoaderWriter.deleteAll(" + iterable + ")");
                            }
                        })
                        .build());

        writeThroughCache.put(42L, "one");
        writeThroughCache.put(43L, "two");
        writeThroughCache.put(44L, "three");
        writeThroughCache.put(45L, "four");


        System.out.println("get(46L): " + writeThroughCache.get(46L));
        writeThroughCache.remove(46L);

        Long[] idArr = {46L, 47L};
        Map<Long, String> map = writeThroughCache.getAll(new HashSet<>(Arrays.asList(idArr)));
        System.out.println("getAll: " + map);

        writeThroughCache.putAll(map);


        writeThroughCache.removeAll(new HashSet<>(Arrays.asList(idArr)));

        if (writeThroughCache.containsKey(42L)) {
            System.out.println("get(42L): " + writeThroughCache.get(42L));
        }




//        for (long i = 0L; i < 120L; ++i) {
//            writeThroughCache.put(i, String.valueOf(i));
//        }
//
//        for (long i = 120L; i >= 0; --i) {
//            System.out.println(i + ":" + writeThroughCache.get(i));
////            sleep(50);
//        }
//
//        for (long i = 0L; i < 1000L; ++i) {
//            System.out.println(i + ":" + writeThroughCache.get(i));
////            sleep(50);
//        }
    }
}
