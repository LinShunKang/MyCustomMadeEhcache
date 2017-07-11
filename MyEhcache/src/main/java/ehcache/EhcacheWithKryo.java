package ehcache;

import cn.lsk.model.WorkExperience;
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

import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/10/17.
 */
public class EhcacheWithKryo {

    private CacheEventListener cacheEventListener = new CacheEventListener<Long, WorkExperience>() {
        public void onEvent(CacheEvent<? extends Long, ? extends WorkExperience> cacheEvent) {
            System.out.println("CacheEventListener: " + cacheEvent.getType() + ", " + cacheEvent.getKey());
        }
    };

    private final CacheEventListenerConfigurationBuilder cacheEventListenerConfBuilder = CacheEventListenerConfigurationBuilder
            .newEventListenerConfiguration(cacheEventListener, EventType.CREATED, EventType.UPDATED, EventType.EVICTED, EventType.REMOVED, EventType.EXPIRED)
            .unordered().asynchronous();

    private final ResourcePoolsBuilder builder = ResourcePoolsBuilder.heap(10).offheap(1, MemoryUnit.MB);
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
    private final CacheConfiguration<Long, WorkExperience> cacheConf = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, WorkExperience.class, builder)
            .withSizeOfMaxObjectGraph(1000)
            .withExpiry(Expirations.timeToLiveExpiration(Duration.of(5, TimeUnit.MINUTES)))
            .add(cacheEventListenerConfBuilder)
            .withValueSerializer(new BZPSerializer<WorkExperience>())
            .build();

    private final Cache<Long, WorkExperience> cache = cacheManager.createCache("GeekWorkExp", cacheConf);

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        EhcacheWithKryo instance = new EhcacheWithKryo();
        Cache<Long, WorkExperience> cache = instance.cache;

        for (long i = 0L; i < 600L; ++i) {
            cache.put(i, WorkExperience.getIntance(i));
        }

        for (long i = 0L; i < 600L; ++i) {
            System.out.println(i + ":" + cache.get(i));
        }
    }

}
