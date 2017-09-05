package MyCustomMadeEhcache.ehcache;

import MyCustomMadeEhcache.model.WorkExperience;
import cache.Cache;
import cache.ehcache.AbstractCacheLoader;
import cache.ehcache.EhcacheBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestCacheLoader {

    private static BlockingQueue<Long> loadKeyQueue = new LinkedBlockingQueue<>();

    private static Cache<Long, List<WorkExperience>> cache = EhcacheBuilder.newBuilder(Long.class, List.class)
            .cacheName("GeekWorkExp")
            .compress(true)
//            .heapPerShard(64)
            .offHeapPerShard(256, MemoryUnit.MB)
            .shardNum(16)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .recordStats()
            .loaderWriter(new AbstractCacheLoader<Long, List>() {
                @Override
                public List load(Long key) throws Exception {
                    System.out.println("thread: " + Thread.currentThread().getName() + ", load(" + key + ")............");
                    loadKeyQueue.add(key);
                    return getList(key, 100);
                }

                @Override
                public Map<Long, List> loadAll(Iterable<? extends Long> keys) throws BulkCacheLoadingException, Exception {
                    System.out.println("keys: " + keys);
                    Map<Long, List> result = new HashMap<>();
                    for (Long key : keys) {
                        result.put(key, getList(key, 100));
                    }
                    return result;
                }
            })
            .build();

    public static void main(String[] args) throws InterruptedException {
//        test(2, 10, 100);
//        testGetIfPresent();
//        compareGetAndGetIfPresent(100000);
//        compareGetAndGetIfPresent(10000000);
        simpleTest();
    }

    private static void test(int putThreadCount, int getThreadCount, int testSize) throws InterruptedException {
        ThreadPoolExecutor putThreadPool = new ThreadPoolExecutor(putThreadCount, putThreadCount, 1, TimeUnit.DAYS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor getThreadPool = new ThreadPoolExecutor(getThreadCount, getThreadCount, 1, TimeUnit.DAYS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
        ThreadPoolExecutor debugThreadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.DAYS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

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
//                        Set<Long> keySet = new HashSet<>();
//                        for (long k = 0; k < 1000; ++k) {
//                            keySet.add(k);
//                        }
//                        Map<Long, List<WorkExperience>> list = cache.getAll(keySet);
                        List<WorkExperience> list = cache.get(key);
                        sum += list.size();
                    }
                    System.out.println("thread:" + Thread.currentThread().getName() + " end!!! sum: " + sum);
                }
            });
        }

        TimeUnit.SECONDS.sleep(4);


        while (true) {
            int duplicateKey = 0;
            List<Long> keyList = new ArrayList<>(12000);
            loadKeyQueue.drainTo(keyList);
            System.out.println("keyList: " + keyList.size());

            Set<Long> hashSet = new HashSet<>();
            for (int i = 0; i < keyList.size(); ++i) {
                Long key = keyList.get(i);
                if (hashSet.contains(key)) {
                    ++duplicateKey;
                    System.out.println("loadKeyQueue is contains: " + key);
                } else {
                    hashSet.add(key);
                }
            }

            System.out.println("duplicateKey: " + duplicateKey);
            System.out.println("cache.stats():" + cache.stats());
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        TimeUnit.DAYS.sleep(10);
    }

    private static List<WorkExperience> getList(long userId, long minId) {
        List<WorkExperience> result = new ArrayList<>(3);
        for (long i = 0L; i < 3; ++i) {
            result.add(WorkExperience.getIntance(userId, minId + i));
        }
        return result;
    }

    private static void testGetIfPresent() {
        System.out.println(cache.getIfPresent(1L));
        System.out.println(cache.get(1L));
        cache.put(1L, getList(1000, 1));
        System.out.println(cache.getIfPresent(1L));
    }

    private static void compareGetAndGetIfPresent(int testTimes) {
        System.out.println(cache.get(1L));

        long startTime = System.currentTimeMillis();
        long sum = 0L;
        for (int i = 0; i < testTimes; ++i) {
            sum += cache.get(1L).size();
        }
        System.out.println("cache.get: cost " + (System.currentTimeMillis() - startTime) + "ms, sum: " + sum);


        sum = 0L;
        for (int i = 0; i < testTimes; ++i) {
            sum += cache.getIfPresent(1L).size();
        }
        System.out.println("cache.getIfPresent: cost " + (System.currentTimeMillis() - startTime) + "ms, sum: " + sum);
    }

    private static void simpleTest() {
        for (int k = 0; k < 10; ++k) {
            for (long i = 0; i < 10; ++i) {
                cache.get(i);
            }
        }

        System.out.println(cache.stats());
    }
}
