/* RunnableDemo.java — Creating threads with Runnable */
public class RunnableDemo {
    static class PrintTask implements Runnable {
        private String taskName;

        PrintTask(String name) {
            this.taskName = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 3; i++) {
                System.out.printf("[%s] Step %d (Thread: %s)%n",
                    taskName, i, Thread.currentThread().getName());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new PrintTask("Download"), "downloader");
        Thread t2 = new Thread(new PrintTask("Process"), "processor");
        Thread t3 = new Thread(new PrintTask("Upload"), "uploader");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
