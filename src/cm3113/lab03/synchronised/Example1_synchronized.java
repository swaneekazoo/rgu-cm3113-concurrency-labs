/* File: Example1_unsynchronizedExercise4.java    Starting point CM3113 Lab4 Exercise 1 */
package cm3113.lab03.synchronised;

/**
 * This version of exercise 1 is unsynchronized. The version suffers from "lost
 * updates" to the shared object countShared. The effect of lost updates is
 * easily verified by running each of the CountingThread threads for a fixed
 * number of iterations and comparing the final sum of theLoopCount for each
 * thread and theTotalLoopCount
 */
public class Example1_synchronized {

    public static void main(String[] args) {
        Counter counter1 = new Counter();
        Counter counter2 = new Counter();
        Counter sharedCounter = new Counter();
		
        CountingThread t1 = new CountingThread(counter1, sharedCounter, "t1");
        CountingThread t2 = new CountingThread(counter2, sharedCounter, "t2");
        t1.start();
        t2.start();

        for (;;) { // wake up main() occasionally and test state of counters
            long count1, count2, countTotal, actualTotal, lost;
			
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
            }
			
			synchronized(sharedCounter) {
				count1 = counter1.getCount();
				count2 = counter2.getCount();
				countTotal = sharedCounter.getCount();
			}
			
			actualTotal = count1 + count2;
			lost = count1 + count2 - countTotal;

            System.out.println("Actual C1 + C2: " + (actualTotal)
                    + ", Recorded C1 + C2 " + countTotal
                    + ", Lost: " + (lost));
        }
    }
}

class Counter {

    private volatile long theCount;

    public Counter() {
        theCount = 0L;
    }

    public synchronized void increment() {
        theCount++;
    }

    public synchronized long getCount() {
        return theCount;
    }
}

class CountingThread extends Thread {

    private Counter thisCounter;
    private Counter sharedCounter;

    public CountingThread(Counter counter, Counter shared, String name) {
        super(name);
        thisCounter = counter;
        sharedCounter = shared;
    }

    public void run() {
        for (;;) {   // start of another loop
            //try {Thread.sleep(10L);} catch (InterruptedException e) {}
            thisCounter.increment();  // count one more loop for this thread
			
			sharedCounter.increment();  // count one more loop for all threads

        }
    }
}
