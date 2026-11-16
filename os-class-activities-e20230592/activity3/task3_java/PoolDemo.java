/* PoolDemo.java — Thread pool with ExecutorService */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PoolDemo {
    static class Task implements Runnable {
        private int taskId;

        Task(int id) {
            this.taskId = id;
        }

        @Override
        public void run() {
            System.out.printf("Task %d started on thread %s%n", taskId, Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int i = 1; i <= 6; i++) {
            pool.submit(new Task(i));
        }
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }
}
