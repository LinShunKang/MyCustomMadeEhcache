package guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import kryo.KryoUtil;
import model.WorkExperience;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by thinker on 7/21/17.
 */

/**
 * 暂不推荐使用该类，因为使用该类会导致内存泄露，
 * 因为当有大量的ByteBuffer存在于老年代、堆外内存使用殆尽并且老年代没有进行垃圾收集时会导致ByteBuffer.allocateDirect()无法分配更多的内存，抛出OutOfMemoryError异常
 */
public class GuavaOffHeapCache<K, V> implements Cache<K, V> {

    private Cache<K, ByteBuffer> byteBufferCache = CacheBuilder.newBuilder().concurrencyLevel(128).expireAfterWrite(1000, TimeUnit.MILLISECONDS).maximumSize(1500000).build();


    @Nullable
    @Override
    public V getIfPresent(Object o) {
        ByteBuffer byteBuffer = byteBufferCache.getIfPresent(o);
        if (byteBuffer != null) {
            byteBuffer = byteBuffer.duplicate();
            byteBuffer.flip();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            return KryoUtil.readFromByteArrayOpt(bytes);
        }
        return null;
    }

    @Override
    public V get(K k, Callable<? extends V> callable) throws ExecutionException {
        return null;
    }

    @Override
    public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
        return null;
    }

    @Override
    public void put(K k, V v) {
        byte[] bytes = KryoUtil.writeToByteArrayOpt(v);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
        byteBufferCache.put(k, byteBuffer);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    }

    @Override
    public void invalidate(Object o) {

    }

    @Override
    public void invalidateAll(Iterable<?> iterable) {

    }

    @Override
    public void invalidateAll() {

    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public CacheStats stats() {
        return null;
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return null;
    }

    @Override
    public void cleanUp() {

    }

    public static void main(String[] args) {
        GuavaOffHeapCache<Long, List<WorkExperience>> cache = new GuavaOffHeapCache<>();
//        test1(1, cache);
//        test2(cache, 4, 1400000, 10);
        test2(cache, 4, 1400000, 10);
    }

    private static void test1(long testSize, GuavaOffHeapCache<Long, List<WorkExperience>> cache) {
        long startTime = System.currentTimeMillis();
        System.out.println("<--------------------put start-------------------->");
        for (long i = 0L; i < testSize; ++i) {
            cache.put(i, getList(i + 1, i));
        }
        System.out.println("<--------------------put end cost:" + (System.currentTimeMillis() - startTime) + "ms-------------------->");

        long totalSize = 0L;
        for (long i = 0L; i < testSize; ++i) {
            List<WorkExperience> workExperiences = cache.getIfPresent(i);
            if (workExperiences != null) {
                totalSize += workExperiences.size();
            }
        }

        System.out.println("totalSize:" + totalSize);
    }

    private static List<WorkExperience> getList(long userId, long minId) {
        List<WorkExperience> result = new ArrayList<>(3);
        for (long i = 0L; i < 3; ++i) {
            result.add(WorkExperience.getIntance(userId, minId + i));
        }
        return result;
    }


    private static void test2(GuavaOffHeapCache<Long, List<WorkExperience>> cache, int threadCount, int testSize, long times) {
        long startTime = System.currentTimeMillis();
        System.out.println("<--------------------put start-------------------->");
        for (long i = 0L; i < testSize; ++i) {
            cache.put(i, getList(i + 1, i));
        }
        System.out.println("<--------------------put end cost:" + (System.currentTimeMillis() - startTime) + "ms-------------------->");

        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadCount, threadCount, 1, TimeUnit.HOURS, new LinkedBlockingQueue<>(1000));
        for (int i = 0; i < threadCount; ++i) {
            threadPoolExecutor.execute(() -> {
                long totalSize = 0L;
                long startTime2 = System.currentTimeMillis();
                for (int k = 0; k < times; ++k) {
                    for (long m = 0L; m < testSize; ++m) {
                        List<WorkExperience> workExpList = cache.getIfPresent(m);
                        if (workExpList != null) {
                            totalSize += workExpList.size();
//                            System.out.println(workExpList);
                        } else {
                            cache.put(m, getList(m + 1, m));
                        }
                    }
                }
                countDownLatch.countDown();
                System.out.println("thread: " + Thread.currentThread().getName() + ", totalSize:" + totalSize + ", cost:" + (System.currentTimeMillis() - startTime2) + "ms");
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
