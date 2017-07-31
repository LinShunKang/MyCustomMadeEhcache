package cache.ehcache;

import kryo.KryoUtil;
import org.ehcache.spi.copy.Copier;

/**
 * Created by LinShunkang on 7/12/17.
 */
public class KryoCopier<T> implements Copier<T> {

    @Override
    public T copyForRead(T obj) {
        if (isImmutableObj(obj)) {
            return obj;
        }
        return KryoUtil.getKryoInstance().copy(obj);
    }

    private boolean isImmutableObj(T obj) {
        return obj instanceof Long
                || obj instanceof String
                || obj instanceof Integer
                || obj instanceof Short
                || obj instanceof Float
                || obj instanceof Double;
    }

    @Override
    public T copyForWrite(T obj) {
        if (isImmutableObj(obj)) {
            return obj;
        }
        return KryoUtil.getKryoInstance().copy(obj);
    }
}
