/* ThreadDemo.java — Creating threads by extending Thread */
public class ThreadDemo {
    static class CounterThread extends Thread {
        private String name;
        private int count;

        CounterThread(String name, int count) {
            this.name = name;
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = 1; i <= count; i++) {
                System.out.printf("[%s] Count: %d (Thread ID: %d)%n",
                    name, i, Thread.currentThread().getId());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CounterThread t1 = new CounterThread("Alpha", 5);
        CounterThread t2 = new CounterThread("Beta", 5);
        CounterThread t3 = new CounterThread("Gamma", 5);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("\nMain thread: all threads finished.");
    }
}
