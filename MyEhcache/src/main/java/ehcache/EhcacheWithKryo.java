package ehcache;

import model.WorkExperience;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheManagerBuilder;

/**
 * Created by LinShunkang on 7/10/17.
 */
public class EhcacheWithKryo {

//    private CacheEventListener cacheEventListener = new CacheEventListener<Long, WorkExperience>() {
//        public void onEvent(CacheEvent<? extends Long, ? extends WorkExperience> cacheEvent) {
//            System.out.println("CacheEventListener: " + cacheEvent.getType() + ", " + cacheEvent.getKey());
//        }
//    };
//
//    private final CacheEventListenerConfigurationBuilder cacheEventListenerConfBuilder = CacheEventListenerConfigurationBuilder
//            .newEventListenerConfiguration(cacheEventListener, EventType.CREATED, EventType.UPDATED, EventType.EVICTED, EventType.REMOVED, EventType.EXPIRED)
//            .unordered().asynchronous();

    private final CacheConfiguration<Long, WorkExperience> cacheConf = EhcacheUtils.getCacheConf(Long.class, WorkExperience.class, 0L, 256L);
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
    private final Cache<Long, WorkExperience> cache = cacheManager.createCache("GeekWorkExp", cacheConf);

    public static void main(String[] args) {
        test1(200);
    }

    private static void test1(long times) {
        EhcacheWithKryo instance = new EhcacheWithKryo();
        Cache<Long, WorkExperience> cache = instance.cache;

        for (long i = 0L; i < times; ++i) {
            cache.put(i, WorkExperience.getIntance(i));
        }

        System.out.println("first times read:--------------------------------");
        for (long i = 0L; i < times; ++i) {
//            System.out.println(i + ":" + cache.get(i));
            cache.get(i);
        }

        System.out.println("second times read:--------------------------------");

        for (long i = 0L; i < times; ++i) {
            cache.get(i);
        }
    }

}
