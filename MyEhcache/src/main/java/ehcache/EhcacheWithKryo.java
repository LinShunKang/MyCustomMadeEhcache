package ehcache;

import model.WorkExperience;
import org.ehcache.Cache;
import org.ehcache.config.units.MemoryUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/10/17.
 */
public class EhcacheWithKryo {

    public static void main(String[] args) throws InterruptedException {
//        testTiredCache();
        testOffHeapVSTiredCache();
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
        List<WorkExperience> result = new ArrayList<>(2);
        for (long i = 0L; i < 2; ++i) {
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
        int testSize = 10000;
        int times = 10000;
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
                .heap(0, MemoryUnit.MB)
                .offHeap(256, MemoryUnit.MB)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();

        testCachePerformance(cache, "testOffHeapCache", testSize, times);
    }

}
