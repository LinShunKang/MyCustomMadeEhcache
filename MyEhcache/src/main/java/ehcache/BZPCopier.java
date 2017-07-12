package ehcache;

import kryo.KryoUtil;
import org.ehcache.spi.copy.Copier;

/**
 * Created by LinShunkang on 7/12/17.
 */
public class BZPCopier<T> implements Copier<T> {

    @Override
    public T copyForRead(T obj) {
        return KryoUtil.getKryoInstance().copy(obj);
    }

    @Override
    public T copyForWrite(T obj) {
        return KryoUtil.getKryoInstance().copy(obj);
    }
}
