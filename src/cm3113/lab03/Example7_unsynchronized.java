package cm3113.lab03;

/* File: Example7_unsynchronized.java    Starting point CM3113 Lab3 Exercise 2 */
/** This version of exercise 2 is unsynchronized. The
 * version suffers from "lost updates" to the shared static variable
 * theTotalLoopCount. The effect of lost updates is easily verified by running
 * each of the CountingThread threads for a fixed number of iterations and
 * comparing the final sum of theLoopCount for each thread and theTotalLoopCount
 */
public class Example7_unsynchronized {

    public static void main(String[] args) {

        CountingThread2 t1 = new CountingThread2("t1");
        CountingThread2 t2 = new CountingThread2("t2");
        t1.start();
        t2.start();
        for (;;) {
            long count1, count2, countTotal;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
			
			synchronized(t1) {
				count1 = t1.getThisCount();
				count2 = t2.getThisCount();
				countTotal = CountingThread2.getSharedCount();
			}
			
            System.out.println("Actual C1 + C2: " + (count1 + count2)
                    + ", Recorded C1 + C2 " + countTotal
                    + ", Lost: " + (count1 + count2 - countTotal));
        }
    }
}

class CountingThread2 extends Thread {

    private static long sharedCount = 0L;
    private long thisCount;

    public CountingThread2(String name) {
        super(name);
        thisCount = 0L;
    }

    public void run() {
        for (;;) {
            increaseThisCount();
            increaseSharedCount();
        }
    }

    public synchronized void increaseThisCount() {
        thisCount++;
    }

    public static synchronized void increaseSharedCount() {
        sharedCount++;
    }

    public long getThisCount() {
        return thisCount;
    }

    public static long getSharedCount() {
        return sharedCount;
    }
}
