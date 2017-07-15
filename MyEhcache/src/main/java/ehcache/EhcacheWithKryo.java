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

    public static void main(String[] args) {
//        test1(2000);
        testTieredCompressCache(10000, 100);
        testTieredCache(10000, 100);
    }

    private static void test1(long times) {
//        Cache<Long, List> cache = EhcacheUtils.getCache(Long.class, List.class, 64L, 256L);
//        for (long i = 0L; i < times; ++i) {
//            cache.put(i, getList(i + 1, i));
//        }
//
//        System.out.println("first times read:--------------------------------");
//        for (long i = 0L; i < times; ++i) {
////            System.out.println(i + ":" + cache.get(i));
//            cache.get(i);
//        }
//
//        System.out.println("second times read:--------------------------------");
//
//        for (long i = 0L; i < times; ++i) {
//            cache.get(i);
//        }

    }

    private static List<WorkExperience> getList(long userId, long minId) {
        List<WorkExperience> result = new ArrayList<>(2);
        for (long i = 0L; i < 2; ++i) {
            result.add(WorkExperience.getIntance(userId, minId + i));
        }
        return result;
    }

    private static void testTieredCompressCache(int testSize, long times) {
//        Cache<Long, List> cache = EhcacheUtils.getCache(Long.class, List.class, 64L, 256L);
//        for (long i = 0L; i < testSize; ++i) {
//            cache.put(i, getList(i + 1, i));
//        }
//
//        long startTime = System.currentTimeMillis();
//        long totalSize = 0L;
//        for (int k = 0; k < times; ++k) {
//            for (long i = 0L; i < testSize; ++i) {
//                totalSize += cache.get(i).size();
//            }
//        }
//
//        System.out.println("testTieredCompressCache(" + testSize + ", " + times + "): totalSize=" + totalSize + ", " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static void testTieredCache(int testSize, long times) {
        Cache<Long, List> cache = EhcacheBuilder.newBuilder().compress(true).heap(64, MemoryUnit.MB).offHeap(256, MemoryUnit.MB).expireAfterAccess(5, TimeUnit.MINUTES).build(Long.class, List.class);

        for (long i = 0L; i < testSize; ++i) {
            cache.put(i, getList(i + 1, i));
        }

        long startTime = System.currentTimeMillis();
        long totalSize = 0L;
        for (int k = 0; k < times; ++k) {
            for (long i = 0L; i < testSize; ++i) {
                totalSize += cache.get(i).size();
            }
        }

        System.out.println("testTieredCache(" + testSize + ", " + times + "): totalSize=" + totalSize + ", " + (System.currentTimeMillis() - startTime) + "ms");
    }

}
