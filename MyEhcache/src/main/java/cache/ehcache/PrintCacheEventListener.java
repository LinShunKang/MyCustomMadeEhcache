package cache.ehcache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 * Created by LinShunkang on 7/11/17.
 */
public class PrintCacheEventListener<K, V> implements CacheEventListener<K, V> {

    @Override
    public void onEvent(CacheEvent cacheEvent) {
        System.out.println("PrintCacheEventListener: " + cacheEvent.getType() + ", " + cacheEvent.getKey());
    }
}
