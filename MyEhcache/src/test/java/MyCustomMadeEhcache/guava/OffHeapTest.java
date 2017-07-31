package MyCustomMadeEhcache.guava;

import MyCustomMadeEhcache.model.WorkExperience;
import cache.guava.GuavaOffHeapCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OffHeapTest {


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
            List<WorkExperience> workExperiences = cache.get(i);
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
