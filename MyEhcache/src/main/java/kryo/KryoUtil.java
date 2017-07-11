package kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.*;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;

public final class KryoUtil {


    //每个线程的 Kryo 实例
    private static final ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();

            /**
             * 不要轻易改变这里的配置！更改之后，序列化的格式就会发生变化，
             * 上线的同时就必须清除 Redis 里的所有缓存，
             * 否则那些缓存再回来反序列化的时候，就会报错
             */
            //支持对象循环引用（否则会栈溢出）
            kryo.setReferences(true); //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置

            //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
            kryo.setRegistrationRequired(false); //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置

            //Fix the NPE bug when deserializing Collections.
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

            return kryo;
        }
    };

    private static final ThreadLocal<Output> outputThreadLocal = new ThreadLocal<Output>() {
        @Override
        protected Output initialValue() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            Output output = new Output(1024, 10240);
            output.setOutputStream(byteArrayOutputStream);
            return output;
        }
    };

    private static final ThreadLocal<UnsafeMemoryOutput> unsafeMemOutputThreadLocal = new ThreadLocal<UnsafeMemoryOutput>() {
        @Override
        protected UnsafeMemoryOutput initialValue() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            UnsafeMemoryOutput output = new UnsafeMemoryOutput(1024, 10240);
            output.setOutputStream(byteArrayOutputStream);
            return output;
        }
    };

    private static final ThreadLocal<UnsafeOutput> unsafeOutputThreadLocal = new ThreadLocal<UnsafeOutput>() {
        @Override
        protected UnsafeOutput initialValue() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            UnsafeOutput output = new UnsafeOutput(1024, 10240);
            output.setOutputStream(byteArrayOutputStream);
            return output;
        }
    };

    private static final ThreadLocal<MyInput> inputThreadLocal = new ThreadLocal<MyInput>() {
        @Override
        protected MyInput initialValue() {
            return new MyInput(4096);
        }
    };

    private static final ThreadLocal<UnsafeMemoryInput> unsafeMemInputThreadLocal = new ThreadLocal<UnsafeMemoryInput>() {
        @Override
        protected UnsafeMemoryInput initialValue() {
            return new UnsafeMemoryInput(4096);
        }
    };

    private static final ThreadLocal<UnsafeInput> unsafeInputThreadLocal = new ThreadLocal<UnsafeInput>() {
        @Override
        protected UnsafeInput initialValue() {
            return new UnsafeInput(4096);
        }
    };


    /**
     * 获得当前线程的 Kryo 实例
     *
     * @return 当前线程的 Kryo 实例
     */
    public static Kryo getInstance() {
        return kryoLocal.get();
    }

    //-----------------------------------------------
    //          序列化/反序列化对象，及类型信息
    //          序列化的结果里，包含类型的信息
    //          反序列化时不再需要提供类型
    //-----------------------------------------------

    /**
     * 将对象【及类型】序列化为字节数组
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字节数组
     */
    public static <T> byte[] writeToByteArray(T obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);

        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, obj);
        output.flush();

        return byteArrayOutputStream.toByteArray();
    }

    public static <T> byte[] writeToByteArrayOpt(T obj) {
        Output output = outputThreadLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) output.getOutputStream();

        try {
            Kryo kryo = getInstance();
            kryo.writeClassAndObject(output, obj);
            output.flush();

            return byteArrayOutputStream.toByteArray();
        } finally {
            output.clear();
            byteArrayOutputStream.reset();
        }
    }


    /**
     * 将字节数组反序列化为原对象
     *
     * @param byteArray writeToByteArray 方法序列化后的字节数组
     * @param <T>       原对象的类型
     * @return 原对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T readFromByteArray(byte[] byteArray) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Input input = new Input(byteArrayInputStream);

        Kryo kryo = getInstance();
        return (T) kryo.readClassAndObject(input);
    }

    @SuppressWarnings("unchecked")
    public static <T> T readFromByteArrayOpt(byte[] byteArray) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        MyInput input = inputThreadLocal.get();
        input.setInputStream(byteArrayInputStream);

        try {
            Kryo kryo = getInstance();
            return (T) kryo.readClassAndObject(input);
        } finally {
            input.rewind();
            input.adjustChars();
        }
    }

    //-----------------------------------------------
    //          只序列化/反序列化对象
    //          序列化的结果里，不包含类型的信息
    //-----------------------------------------------

    /**
     * 将对象序列化为字节数组
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字节数组
     */
    public static <T> byte[] writeObjectToByteArray(T obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);

        Kryo kryo = getInstance();
        kryo.writeObject(output, obj);
        output.flush();

        return byteArrayOutputStream.toByteArray();
    }

    public static <T> byte[] writeObjectToByteArrayOpt(T obj) {
        Output output = outputThreadLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) output.getOutputStream();
        try {
            Kryo kryo = getInstance();
            kryo.writeObject(output, obj);
            output.flush();

            return byteArrayOutputStream.toByteArray();
        } finally {
            output.clear();
            byteArrayOutputStream.reset();
        }
    }

    public static <T> byte[] writeObjectToByteArrayOpt2(T obj) {
        UnsafeMemoryOutput output = unsafeMemOutputThreadLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) output.getOutputStream();
        try {
            Kryo kryo = getInstance();
            kryo.writeObject(output, obj);
            output.flush();

            return byteArrayOutputStream.toByteArray();
        } finally {
            output.clear();
            byteArrayOutputStream.reset();
        }
    }

    public static <T> byte[] writeObjectToByteArrayOpt3(T obj) {
        UnsafeOutput output = unsafeOutputThreadLocal.get();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) output.getOutputStream();
        try {
            Kryo kryo = getInstance();
            kryo.writeObject(output, obj);
            output.flush();

            return byteArrayOutputStream.toByteArray();
        } finally {
            output.clear();
            byteArrayOutputStream.reset();
        }
    }


    /**
     * 将字节数组反序列化为原对象
     *
     * @param byteArray writeToByteArray 方法序列化后的字节数组
     * @param clazz     原对象的 Class
     * @param <T>       原对象的类型
     * @return 原对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromByteArray(byte[] byteArray, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Input input = new Input(byteArrayInputStream);

        Kryo kryo = getInstance();
        return kryo.readObject(input, clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromByteArrayOpt(byte[] byteArray, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        MyInput input = inputThreadLocal.get();
        input.setInputStream(byteArrayInputStream);

        try {
            Kryo kryo = getInstance();
            return kryo.readObject(input, clazz);
        } finally {
            input.rewind();
            input.adjustChars();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromByteArrayOpt2(byte[] byteArray, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        UnsafeMemoryInput input = unsafeMemInputThreadLocal.get();
        input.setInputStream(byteArrayInputStream);

        try {
            Kryo kryo = getInstance();
            return kryo.readObject(input, clazz);
        } finally {
            input.rewind();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromByteArrayOpt3(byte[] byteArray, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        UnsafeInput input = unsafeInputThreadLocal.get();
        input.setInputStream(byteArrayInputStream);

        try {
            Kryo kryo = getInstance();
            return kryo.readObject(input, clazz);
        } finally {
            input.rewind();
        }
    }
}