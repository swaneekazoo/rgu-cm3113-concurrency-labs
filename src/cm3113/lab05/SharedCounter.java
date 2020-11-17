package cm3113.lab05;

// File: SharedCounter.java used in CM3113 Lab 5 case study 1 
public class SharedCounter extends Counter { 
  /* This class performs arithmetic on shared variables. It is used to test 
   * mutual exclusion algorithms that are NOT safe, so uses atomic integer 
   * and long to ensure that the arithmetic is not subject to lost updates */
  private java.util.concurrent.atomic.AtomicLong mutexViolations ;
  private java.util.concurrent.atomic.AtomicInteger criticalThreads ;
  
  public SharedCounter() { 
    super() ; 
    this.reset();
  }
  
  public void reset(){
      setValue(0L);
      mutexViolations = new java.util.concurrent.atomic.AtomicLong(0L);
      criticalThreads = new java.util.concurrent.atomic.AtomicInteger(0);
  }
  
  public void enter() { 
    if (criticalThreads.get() > 0) setValue(mutexViolations.incrementAndGet());
    criticalThreads.incrementAndGet();
  }
  
  public void exit() { 
      criticalThreads.decrementAndGet();
  }
} 
