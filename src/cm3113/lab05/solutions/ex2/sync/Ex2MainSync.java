package cm3113.lab05.solutions.sync;
/* File: Ex2Main.java used in CM3113 Lab 5 Exercise 2
 * Code has unprotected access to shared data */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Ex2MainSync {
  public static final int NPRODUCERS = 2 ; 
  
  public static void main(String[] args) { 
    List buffer = new ArrayList<>();

    cm3113.lab05.solutions.ex24.Consumer c = new cm3113.lab05.solutions.ex24.Consumer(NPRODUCERS, buffer);
    c.start() ;
    
    cm3113.lab05.solutions.ex24.Producer[] p = new cm3113.lab05.solutions.ex24.Producer[NPRODUCERS] ;
    for (int i = 0 ; i < NPRODUCERS ; i++) { 
      p[i] = new cm3113.lab05.solutions.ex24.Producer(i, NPRODUCERS, buffer);
      p[i].start() ;  
    }    
  }  
}

/* *************************************************************** */

class Producer extends Thread { 
  private int theID ; 
  private int numProducers ;
  private List theBuffer ;

  public Producer(int id, int nproducers, List buf) {
    theBuffer = buf ; 
    theID = id ; 
    numProducers = nproducers ;
  }
  
  @Override
  public void run() { 
    long msgNum = theID ; 
    for(;;) {
      synchronized (theBuffer) {
        theBuffer.add(msgNum);    // send the message
        msgNum += numProducers;   // prepare the next message
      }

      try {  /* slow down Producer otherwise we may get an out of memory 
              * error if Consumer cannot empty the buffer fast enough */
        Thread.sleep(50L);
      } catch (InterruptedException ie){}
    } 
  } 
} 

/* *************************************************************** */

class Consumer extends Thread {
  private int numProducers;
  private List theBuffer;
  public Consumer(int nproducers, List buf) {
    theBuffer = buf;
    numProducers = nproducers;
    this.setDaemon(true);
  }

  @Override
  public void run() {
    int producerID;
    long msg;
    long[] expectedMessageNum = new long[]{0, 1};
    long count = 0L, max = 0L;

    DateFormat df = new SimpleDateFormat("HH:mm:ss:SSS");
    // get current time
    long startTime = System.currentTimeMillis();
    System.out.println("Consumer thread starts at " + df.format(startTime));

    for (; ; ) {
      if (++count % 100000 == 0) { // check periodically for buffer size

        synchronized (theBuffer) {
          System.out.println("consumer checked:" + count
                  + " times, buffer size was: " + theBuffer.size()
                  + " largest buffer size has been was: " + max
                  + " after " + (System.currentTimeMillis() - startTime) + "ms");

          if (theBuffer.size() > 0) { // check if there is a message in buffer
            if (theBuffer.size() > max) max = theBuffer.size();
            try { // try to extract message from buffer
              msg = (long) theBuffer.remove(0);
              producerID = (int) (msg % numProducers); // which producer sent msg

              if (msg != expectedMessageNum[producerID]) { // check for lost msg
                System.out.println(
                        "Problem! Expected: " + (expectedMessageNum[producerID]) +
                                " received: " + msg + " from producer " + producerID);
              }
              expectedMessageNum[producerID] = msg + numProducers;
            } catch (Exception e) {
              System.out.println("Error reading buffer !!");
            }
          } else {
            Thread.yield();
          }
        }
      }
    }
  }
}

