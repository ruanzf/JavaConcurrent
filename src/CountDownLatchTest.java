import java.util.concurrent.CountDownLatch;

/**
 * Created by ruan on 2016/4/30.
 * await(1)、(2)
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        int N = 4;
        CountDownLatch latch = new CountDownLatch(N);
        for(int i=0;i<N;i++)
            new Writer(latch).start();

        /**
         * (2) 可以在这使用一个await 等待所有线程结束
         * 可能会出现以下结果
         *      Thread-1 end
         *      test done
         *      Thread-1 done
         *      因为所有N个Writer线程都countDown后是
         *      有N+1个线程竞争CPU
         */
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("test done");
    }

    static class Writer extends Thread{
        CountDownLatch latch;
        public Writer(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println(String.format("%s begin", Thread.currentThread().getName()));
            try {
                Thread.sleep((int)(Math.random() * 10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("%s end", Thread.currentThread().getName()));
            latch.countDown();

            /**
             * (1)
             * 第一个线程end并countDown后，在这等。
             * 所有线程都countDown后，才能继续执行
             *
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */

            System.out.println(String.format("%s done", Thread.currentThread().getName()));
        }
    }
}