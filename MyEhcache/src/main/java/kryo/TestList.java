package kryo;

import com.esotericsoftware.kryo.Kryo;
import model.WorkExperience;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by LinShunkang on 7/9/17.
 */
public class TestList {

    public static void main(String[] args) {
        Kryo kryo = KryoUtil.getInstance();


//
//        List<WorkExperience> listCopy = kryo.copy(list);
//        System.out.println(listCopy);
//
//        WorkExperience workExp = WorkExperience.getIntance(1111L);
//        WorkExperience workExpCopy = kryo.copy(workExp);
//        System.out.println(workExpCopy);

        int times = 100000;
        benchmarkTest(times);
        sleep(3000);
        benchmarkTest(times * 10);
    }

    public static List<WorkExperience> getList(int size) {
        ArrayList<WorkExperience> list = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            list.add(WorkExperience.getIntance(i));
        }
        return list;
    }

    public static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void benchmarkTest(int times) {
        List<WorkExperience> list = getList(3);

        testDirectCopy(list, times);

        System.gc();
        sleep(3000);

        testSerilizedCopy(list, times);
    }

    private static void testDirectCopy(List<WorkExperience> list, int times) {
        long startTime = System.currentTimeMillis();
        Kryo kryo = KryoUtil.getInstance();
        int totalSize = 0;
        for (int i = 0; i < times; ++i) {
            List<WorkExperience> listCopy = kryo.copy(list);
            totalSize += listCopy.size();
        }
        System.out.println("testDirectCopy(" + list.size() + ", " + times + "): totalSize: " + totalSize + ", cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }


    private static void testSerilizedCopy(List<WorkExperience> list, int times) {
        byte[] bytes = KryoUtil.writeObjectToByteArray(list);

        long startTime = System.currentTimeMillis();
        int totalSize = 0;
        for (int i = 0; i < times; ++i) {
            List<WorkExperience> listCopy = KryoUtil.readObjectFromByteArray(bytes, list.getClass());
            totalSize += listCopy.size();
        }
        System.out.println("testSerilizedCopy(" + list.size() + ", " + times + "): totalSize: " + totalSize + ", cost: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
