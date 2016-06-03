import java.util.concurrent.Exchanger;

/**
 * Created by ruan on 2016/4/30.
 */
public class ExchangerTest {

    Exchanger<Integer> exchanger = new Exchanger<Integer>();
    Integer minusInteger = new Integer(10);
    Integer plusInteger = new Integer(-10);

    /**
     * 以正整数和PlusLoop交换
     * 每次得到以正整数和PlusLoop交换的负整数后，先取反再减减
     * 直到0为止
     *
     */
    class MinusLoop implements Runnable {

        private String name;
        public MinusLoop(String name) {
            this.name = name;
        }

        public void run() {
            Integer currentBuffer = minusInteger;
            try {
                while (currentBuffer != null) {
                    System.out.println(name + ", before exchange, I'm " + currentBuffer);
                    currentBuffer = exchanger.exchange(currentBuffer);
                    System.out.println(name + ", after exchange, I'm " + currentBuffer);
                    if(currentBuffer == 0)
                        break;
                    if(currentBuffer < 0) {
                        currentBuffer = -currentBuffer;
                        currentBuffer --;
                    }

                    /**
                     * 虽然我休息很短的时间
                     * 但是在进行交换时
                     * 我还是要等那位交换者
                     */
                    Thread.sleep(30);
                }
                System.out.println(name + ", Im done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 以负整数和PlusLoop交换
     * 每次得到PlusLoop的正整数后，先取反再加加
     * 直到0为止
     *
     */
    class PlusLoop implements Runnable {

        private String name;
        public PlusLoop(String name) {
            this.name = name;
        }

        public void run() {
            Integer currentBuffer = plusInteger;
            try {
                while (currentBuffer != null) {
                    System.out.println(name + ", before exchange, I'm " + currentBuffer);
                    currentBuffer = exchanger.exchange(currentBuffer);
                    System.out.println(name + ", after exchange, I'm " + currentBuffer);
                    if(currentBuffer == 0)
                        break;
                    if(currentBuffer > 0) {
                        currentBuffer = -currentBuffer;
                        currentBuffer ++;
                    }

                    /**
                     * 虽然我休息很长的时间
                     * 但是在进行交换时
                     * 别人还是要一直等我
                     */
                    Thread.sleep(10000);
                }
                System.out.println(name + ", Im done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void start() {
        /**
         * MMMMMMMMMMMMMMMMMMMMMMMM
         * pppppppp
         * 为了输出时好区分
         */
        new Thread(new MinusLoop("MMMMMMMMMMMMMMMMMMMMMMMM")).start();
        new Thread(new PlusLoop("pppppppp")).start();
    }

    public static void main(String[] args) {
        new ExchangerTest().start();
    }
}