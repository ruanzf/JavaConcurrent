import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by ruan on 2016/4/30.
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {

        int N = 4;
        CyclicBarrier barrier = new CyclicBarrier(N, new Runnable() {
            @Override
            public void run() {
                System.out.println("this is call back");
            }
        });

        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }
    static class Writer extends Thread{

        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
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

            try {
                this.cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println(String.format("%s done", Thread.currentThread().getName()));
        }
    }
}
