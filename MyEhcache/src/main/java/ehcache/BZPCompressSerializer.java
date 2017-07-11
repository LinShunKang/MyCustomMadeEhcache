package ehcache;

import kryo.KryoUtil;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by LinShunkang on 7/10/17.
 */
public class BZPCompressSerializer<T> implements Serializer<T> {

    @Override
    public ByteBuffer serialize(Object o) throws SerializerException {
        byte[] bytes = KryoUtil.writeToByteArrayOpt(o);
        try {
            byte[] compressBytes = Snappy.compress(bytes);
            return ByteBuffer.wrap(compressBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T read(ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        try {
            byte[] uncompressBytes = Snappy.uncompress(bytes);
            return KryoUtil.readFromByteArrayOpt(uncompressBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o, ByteBuffer byteBuffer) throws ClassNotFoundException, SerializerException {
        return o.equals(read(byteBuffer));
    }
}
