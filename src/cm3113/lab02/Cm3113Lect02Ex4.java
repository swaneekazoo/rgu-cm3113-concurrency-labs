package cm3113.lab02;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Cm3113Lect02Ex4 {
  // my home PC has quad core processor so set size of thread pool to 4
  private static final int NTHREADS = 128;

  public static void main(String[] args) {
    LocalTime startTime = LocalTime.now();
    System.out.println("Main thread starts at " + startTime) ;

    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    List<Future<String>> list = new ArrayList<Future<String>>();
    int NTASKS = 1000;

    for (int i = 0; i < NTASKS; i++) {
      Callable<String> task = new MyCallable(100000000L, i);
      Future<String> future = executor.submit(task);
      list.add(future);
    }

    String sum;
    // now retrieve the results from the Callable tasks from the Future objects
    for (Future<String> future : list) {
      try {
        sum = future.get();
        System.out.println(sum);
      } catch (InterruptedException e) {  e.printStackTrace(); }
      catch (ExecutionException e) { e.printStackTrace(); }
    }
    executor.shutdown();  // shutdown() prevents the executor from accepting
                //new Callable tasks but allows all existing tasks to finish
    try {
        // Wait until all threads are finished, awaitTermination blocks until
        // all tasks have finished, or until specified time has passed
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)){
            // shutdownNow() stops all tasks immediately
            executor.shutdownNow();
            System.out.println("I am not going to wait any longer than 60s");
        }
    } catch(InterruptedException ie){
        executor.shutdownNow();
        System.out.println("Was interrupted while waiting");
    }

    LocalTime endTime = LocalTime.now();
    System.out.println("Main thread ends at " + endTime + " after "
            + (Duration.between(startTime, endTime).toMillis()) + " ms")  ;

  }
}
