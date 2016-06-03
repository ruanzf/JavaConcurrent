import java.util.concurrent.Semaphore;

/**
 * Created by ruan on 2016/4/30.
 */
public class SemaphoreTest {

    private static final int MAX_AVAILABLE = 5;

//    公平队列
    private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

//    非公平队列
//    private static final Semaphore available = new Semaphore(MAX_AVAILABLE);
    private static SemaphoreTest semaphoreTest = new SemaphoreTest();

    public static void main(String[] args) throws InterruptedException {
        int N = 20;
        for (int i=0; i<N;i++) {
            new Writer().start();
        }
    }

    public Integer getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    public void putItem(Object x) {
        if (markAsUnused(x))
            available.release();
    }


    protected Integer[] items = new Integer[MAX_AVAILABLE];
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    protected synchronized Integer getNextAvailableItem() {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                System.out.println(String.format("%s I got %s", Thread.currentThread().getName(), i));
                used[i] = true;
                items[i] = new Integer(i);
                return items[i];
            }
        }
        return null;
    }

    protected synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    System.out.println(String.format("%s I return %s", Thread.currentThread().getName(), i));
                    used[i] = false;
                    return true;
                } else
                    return false;
            }
        }
        return false;
    }

    static class Writer extends Thread{

        @Override
        public void run() {
            try {
                Integer i = semaphoreTest.getItem();
                Thread.sleep((int)(Math.random() * 10000));
                semaphoreTest.putItem(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}