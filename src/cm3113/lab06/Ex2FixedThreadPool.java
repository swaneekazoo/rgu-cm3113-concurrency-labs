package cm3113.lab06;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex2FixedThreadPool {
    private static final int NTHREADS = 8;
    private static final int NTASKS = 50;

    public static void main(String[] args) {
        // use Executors factory method to create thread pool
        ExecutorService pool = Executors.newFixedThreadPool(NTHREADS);
        Task[] tasks = new Task[NTASKS];
        long start = System.currentTimeMillis();
        for (int i = 0; i < NTASKS; i++) {
            tasks[i] = new Task(i);
            pool.submit(tasks[i]);
            System.out.println("task: " + i + " queued for execution");
        }
        pool.shutdown();
// wait for pool to shut down
        while (!pool.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        System.out.println("Finished all tasks after " + (end - start) + "ms");
    }
}
