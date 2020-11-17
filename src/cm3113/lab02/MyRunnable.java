package cm3113.lab02;

import java.time.LocalTime;

public class MyRunnable implements Runnable {
  private final long countUntil;
  private final int id;

  MyRunnable(long countUntil, int id) {
    this.countUntil = countUntil;
    this.id = id;
  }

  @Override
  public void run() {
    long sum = 0;
    for (long i = 1; i < countUntil; i++) {
      sum += i; //just a bit of arithmetic to give the loop something to do
    }
    System.out.println("  task " + id + " completed at " + LocalTime.now());
  }
}

