package ehcache;


import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 * Created by LinShunkang on 7/11/17.
 */
public class BZPCacheEventListener<K, V> implements CacheEventListener<K, V> {

    @Override
    public void onEvent(CacheEvent cacheEvent) {
        //TODO:可以加一个CacheStat来计算
        System.out.println("CacheEventListener: " + cacheEvent.getType() + ", " + cacheEvent.getKey());
    }
}
