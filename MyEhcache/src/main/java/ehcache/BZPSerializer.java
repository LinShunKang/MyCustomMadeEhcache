package ehcache;

import kryo.KryoUtil;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;

import java.nio.ByteBuffer;

/**
 * Created by LinShunkang on 7/10/17.
 */
public class BZPSerializer<T> implements Serializer<T> {

    @Override
    public ByteBuffer serialize(Object o) throws SerializerException {
        byte[] bytes = KryoUtil.writeToByteArrayOpt(o);
        return ByteBuffer.wrap(bytes);
    }

    @Override
    public T read(ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return KryoUtil.readFromByteArrayOpt(bytes);
    }

    @Override
    public boolean equals(Object o, ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        return o.equals(read(byteBuffer));
    }
}
