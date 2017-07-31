package cache;

import java.util.Map;
import java.util.Set;

public interface Cache<K, V> {

    V get(K key);

    void put(K key, V value);

    void remove(K key);

    Map<K, V> getAll(Set<? extends K> keys);

    void putAll(Map<? extends K, ? extends V> entries);

    void removeAll(Set<? extends K> keys);

    void clear();

    CacheStats stats();
}
