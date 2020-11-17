package cm3113.lab02;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Use high level threading
 * Create thread pool and submit tasks as Runnable instances
 * and test for termination using Thread.isAlive()
 */
public class Cm3113Lect02Ex3 {
  // my home PC has quad core processor, so set size of thread pool to 4
  private static final int NTHREADS = 16;

  public static void main(String[] args) {
    LocalTime startTime = LocalTime.now();
    System.out.println("Main thread starts at " + startTime) ;
    
    // creates a pool of threads into which Runnable tasks can be placed
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    int NTASKS = 1000;
    for (int i = 0; i < NTASKS; i++) {
      Runnable task = new MyRunnable(100000000L, i);
      executor.execute(task);
      Future f = executor.submit(task);
    }
    // shutdown() will prevent the executor from accepting new Runnable tasks
    // but allows all existing tasks to finish
    executor.shutdown();
    
    try {
    // Wait until all threads are finished, awaitTermination blocks until all 
    // tasks have finished, or until specified time has passed    
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)){
            // shutdownNow() stops all tasks immediately
            executor.shutdownNow();
            System.out.println("I am not going to wait any longer than 60s");
        }
    } catch(InterruptedException ie){
        executor.shutdownNow();
        System.out.println("Was interrupted while waiting");
    }
    System.out.println("All threads finished");
    
    LocalTime endTime = LocalTime.now();
    System.out.println("Main thread ends at " + endTime + " after " 
            + (Duration.between(startTime, endTime).toMillis()) + " ms")  ;
  }
    
}
