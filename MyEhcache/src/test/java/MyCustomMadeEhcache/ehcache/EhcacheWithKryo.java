package MyCustomMadeEhcache.ehcache;

import cache.Cache;
import cache.ehcache.PrintCacheEventListener;
import cache.ehcache.EhcacheBuilder;
import MyCustomMadeEhcache.model.WorkExperience;
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
//        testOffHeapCacheConcurrent(4, 1400000, 10);
//        testShardPerf(4, 140, 10);
//        testShardPerf(10, 1400000, 2);

        testAllTheTime(2, 10, 10000);
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
                .heapPerShard(64, MemoryUnit.MB)
                .offHeapPerShard(256, MemoryUnit.MB)
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
                .heapPerShard(64, MemoryUnit.MB)
                .offHeapPerShard(256, MemoryUnit.MB)
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
                .heapPerShard(1, MemoryUnit.MB)
                .offHeapPerShard(2, MemoryUnit.MB)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();

        testCachePerformance(cache, "testOffHeapCache", testSize, times);
    }

    private static void testOffHeapCacheConcurrent(int threadCount, int testSize, long times) {
        Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp4")
//                .compress(true)
                .compress(false)
                .heapPerShard(0, MemoryUnit.MB)
                .offHeapPerShard(2, MemoryUnit.MB)
                .expireAfterWrite(10, TimeUnit.HOURS)
                .cacheEventListener(new PrintCacheEventListener<>(), EventType.EVICTED, EventType.EXPIRED)
                .build();

        testConcurrent(cache, threadCount, testSize, times);
    }

    private static void testConcurrent(Cache<Long, List<WorkExperience>> cache, int threadCount, int testSize, long times) {
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
                System.out.println("thread: " + Thread.currentThread().getName() + ", totalSize:" + totalSize + ", cost:" + (System.currentTimeMillis() - startTime2) + "ms, cache.stats(): " + cache.stats());
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

    private static void testShardPerf(int threadCount, int testSize, long times) throws InterruptedException {
        Cache<Long, List<WorkExperience>> cache1 = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp5")
                .shardNum(1)
                .compress(false)
                .heapPerShard(0, MemoryUnit.MB)
                .offHeapPerShard(2 * 1024, MemoryUnit.MB)
                .expireAfterWrite(10, TimeUnit.HOURS)
                .cacheEventListener(new PrintCacheEventListener<>(), EventType.EVICTED, EventType.EXPIRED)
                .build();
        testConcurrent(cache1, threadCount, testSize, times);
//        testCachePerformance(cache1, "GeekWorkExp5", testSize, testSize);

        System.gc();
        TimeUnit.SECONDS.sleep(3);

        Cache<Long, List<WorkExperience>> cache2 = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp6")
                .shardNum(16)
                .compress(false)
                .heapPerShard(0, MemoryUnit.MB)
                .offHeapPerShard(2 * 1024, MemoryUnit.MB)
                .expireAfterWrite(10, TimeUnit.HOURS)
                .cacheEventListener(new PrintCacheEventListener<>(), EventType.EVICTED, EventType.EXPIRED)
                .build();
        testConcurrent(cache2, threadCount, testSize, times);
//        testCachePerformance(cache2, "GeekWorkExp5", testSize, testSize);
    }

    private static void testAllTheTime(int putThreadCount, int getThreadCount, int testSize) throws InterruptedException {
        Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
                .cacheName("GeekWorkExp7")
                .shardNum(16)
                .compress(true)
                .heapPerShard(0, MemoryUnit.MB)
                .offHeapPerShard(256, MemoryUnit.MB)
                .expireAfterWrite(1, TimeUnit.HOURS)
//                .cacheEventListener(new PrintCacheEventListener<>(), EventType.EVICTED, EventType.EXPIRED)
                .build();

        ThreadPoolExecutor putThreadPool = new ThreadPoolExecutor(putThreadCount, putThreadCount, 1, TimeUnit.DAYS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor getThreadPool = new ThreadPoolExecutor(getThreadCount, getThreadCount, 1, TimeUnit.DAYS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

//        for (int i = 0; i < putThreadCount; ++i) {
//            putThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("putThread:" + Thread.currentThread().getName() + " start!!!");
//                    for (long i = 0; i < Long.MAX_VALUE; ++i) {
//                        long key = i % testSize;
//                        if (key / 1000 == 0) {
//                            cache.put(key, getList(key, key + 1));
//                        }
//                    }
//                    System.out.println("putThread:" + Thread.currentThread().getName() + " end!!!");
//                }
//            });
//        }

        for (int i = 0; i < getThreadCount; ++i) {
            getThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("getThread:" + Thread.currentThread().getName() + " start!!!");
                    long sum = 0L;
                    for (long i = 0; i < Long.MAX_VALUE; ++i) {
                        long key = i % testSize;
                        List<WorkExperience> list = cache.get(key);
                        if (list == null) {
                            System.out.println("getThread:" + Thread.currentThread().getName() + " put key: " + key + "!!!!");
                            cache.put(key, getList(key, key + 1));
                        } else {
                            sum += list.size();
                        }
                    }
                    System.out.println("thread:" + Thread.currentThread().getName() + " end!!! sum: " + sum);
                }
            });
        }

        TimeUnit.DAYS.sleep(10);
    }
}
