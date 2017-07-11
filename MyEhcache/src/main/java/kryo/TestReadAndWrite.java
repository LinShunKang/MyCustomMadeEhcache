package kryo;

import model.WorkExperience;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by LinShunkang on 7/9/17.
 */
public class TestReadAndWrite {

    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 1, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>(10000), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) throws InterruptedException {
        int times = 100000;

        TestList.sleep(30000);

//        benchmarkTest(1);
//
//        TestList.sleep(3000);
//        benchmarkTest(times / 10);
//
//        TestList.sleep(3000);
//        benchmarkTest(times);
//
//        TestList.sleep(3000);
//        benchmarkTest(times * 10);
//
//        TestList.sleep(3000);
        benchmarkTest(times * 100);

        threadPool.shutdown();
        threadPool.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static void benchmarkTest(int times) {
        List<WorkExperience> list = TestList.getList(10);

//        testNormal(list, times);
//
//        System.gc();
//        TestList.sleep(3000);
//
        testOpt(list, times);

        System.gc();
        TestList.sleep(3000);

        testOpt2(list, times);

        System.gc();
        TestList.sleep(3000);

        testOpt3(list, times);
    }

    private static void testNormal(final List<WorkExperience> list, int times) {
        long startTime = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(times);
        final AtomicLong totalListSize = new AtomicLong(0);
        final AtomicLong totalBytesLength = new AtomicLong(0L);
        for (int i = 0; i < times; ++i) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes = KryoUtil.writeObjectToByteArray(list);
                        totalBytesLength.addAndGet(bytes.length);

                        List<WorkExperience> listCopy = KryoUtil.readObjectFromByteArray(bytes, list.getClass());
                        totalListSize.addAndGet(listCopy.size());
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }

        System.out.println("testNormal(" + list.size() + ", " + times + "): totalListSize: " + totalListSize + ", totalBytesLength: " + totalBytesLength + ", cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }


    private static void testOpt(final List<WorkExperience> list, int times) {
        long startTime = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(times);
        final AtomicLong totalListSize = new AtomicLong(0L);
        final AtomicLong totalBytesLength = new AtomicLong(0L);
        for (int i = 0; i < times; ++i) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes = KryoUtil.writeObjectToByteArrayOpt(list);
                        totalBytesLength.addAndGet(bytes.length);

                        List<WorkExperience> listCopy = KryoUtil.readObjectFromByteArrayOpt(bytes, list.getClass());
                        totalListSize.addAndGet(listCopy.size());
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }
        System.out.println("testOpt(" + list.size() + ", " + times + "): totalListSize: " + totalListSize + ", totalBytesLength: " + totalBytesLength + ", cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static void testOpt2(final List<WorkExperience> list, int times) {
        long startTime = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(times);
        final AtomicLong totalListSize = new AtomicLong(0L);
        final AtomicLong totalBytesLength = new AtomicLong(0L);
        for (int i = 0; i < times; ++i) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes = KryoUtil.writeObjectToByteArrayOpt2(list);
                        totalBytesLength.addAndGet(bytes.length);

                        List<WorkExperience> listCopy = KryoUtil.readObjectFromByteArrayOpt2(bytes, list.getClass());
                        totalListSize.addAndGet(listCopy.size());
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }
        System.out.println("testOpt2(" + list.size() + ", " + times + "): totalListSize: " + totalListSize + ", totalBytesLength: " + totalBytesLength + ", cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static void testOpt3(final List<WorkExperience> list, int times) {
        long startTime = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(times);
        final AtomicLong totalListSize = new AtomicLong(0L);
        final AtomicLong totalBytesLength = new AtomicLong(0L);
        for (int i = 0; i < times; ++i) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] bytes = KryoUtil.writeObjectToByteArrayOpt3(list);
                        totalBytesLength.addAndGet(bytes.length);

                        List<WorkExperience> listCopy = KryoUtil.readObjectFromByteArrayOpt3(bytes, list.getClass());
                        totalListSize.addAndGet(listCopy.size());
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }
        System.out.println("testOpt3(" + list.size() + ", " + times + "): totalListSize: " + totalListSize + ", totalBytesLength: " + totalBytesLength + ", cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
