package ehcache;

import model.WorkExperience;
import org.ehcache.Cache;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.event.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/10/17.
 */
public class EhcacheWithKryo {

    public static void main(String[] args) throws InterruptedException {
//        testTiredCache();
//        testOffHeapVSTiredCache();
//        testOffHeapCacheConcurrent(4, 1760000, 10);
        testOffHeapCacheConcurrent(4, 1400000, 10);
    }

    private static void testTiredCache() throws InterruptedException {
        testTieredCompressCache(10000, 100);
//        testTieredCache(10000, 100);

        System.gc();
        TimeUnit.SECONDS.sleep(3);

        testTieredCache(10000, 100);
//        testTieredCompressCache(10000, 100);
    }

    private static List<WorkExperience> getList(long userId, long minId) {
        List<WorkExperience> result = new ArrayList<>(3);
        for (long i = 0L; i < 3; ++i) {
            result.add(WorkExperience.getIntance(userId, minId + i));
        }
        return result;
    }

    private static void testTieredCompressCache(int testSize, long times) {
        Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp")
                .compress(true)
                .heap(64, MemoryUnit.MB)
                .offHeap(256, MemoryUnit.MB)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();

        testCachePerformance(cache, "testTieredCompressCache", testSize, times);
    }

    private static void testCachePerformance(Cache<Long, List<WorkExperience>> cache, String cacheName, int testSize, long times) {
        long startTime = System.currentTimeMillis();
        for (long i = 0L; i < testSize; ++i) {
            cache.put(i, getList(i + 1, i));
        }

        long totalSize = 0L;
        for (int k = 0; k < times; ++k) {
            for (long i = 0L; i < testSize / 2; ++i) {
                List<WorkExperience> workExperiences = cache.get(i);
                if (workExperiences != null) {
                    totalSize += workExperiences.size();
                }
            }
        }

        System.out.println("testCachePerformance(" + cacheName + ", " + testSize + ", " + times + "): totalSize=" + totalSize + ", " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static void testTieredCache(int testSize, long times) {
        Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp2")
                .compress(false)
                .heap(64, MemoryUnit.MB)
                .offHeap(256, MemoryUnit.MB)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();

        testCachePerformance(cache, "testTieredCache", testSize, times);
    }

    private static void testOffHeapVSTiredCache() throws InterruptedException {
        int testSize = 10;
        int times = 1;
        testOffHeapCache(testSize, times);
//        testTieredCache(testSize, 100);

        System.gc();
        TimeUnit.SECONDS.sleep(3);

        testTieredCache(testSize, times);
//        testOffHeapCache(testSize, 100);
    }

    private static void testOffHeapCache(int testSize, long times) {
        Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp3")
                .compress(false)
                .heap(1, MemoryUnit.MB)
                .offHeap(2, MemoryUnit.MB)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();

        testCachePerformance(cache, "testOffHeapCache", testSize, times);
    }

    private static void testOffHeapCacheConcurrent(int threadCount, int testSize, long times) {
        Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp4")
//                .compress(true)
                .compress(false)
                .heap(0, MemoryUnit.MB)
//                .offHeap(3, MemoryUnit.GB)
                .offHeap(2, MemoryUnit.MB)
                .expireAfterWrite(10, TimeUnit.HOURS)
                .cacheEventListener(new BZPCacheEventListener<>(), EventType.EVICTED, EventType.EXPIRED)
                .build();

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
                        List<WorkExperience> workExpList = cache.get(m);
                        if (workExpList != null) {
                            totalSize += workExpList.size();
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
