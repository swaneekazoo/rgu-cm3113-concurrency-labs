package cm3113.lab05;

import java.util.concurrent.Semaphore;

/* File: CountingThread.java
 * Starting Point to CM3113 Lab 5 Exercise 1 */
public class CountingThreadSem extends Thread {

    private final Counter label;
    private SharedCounter sharedLabel;
    private Semaphore sem;
    private long threadLoopCount;
    private final long critDelay;
    private final long restDelay;
    private volatile boolean stopFlag;

    public CountingThreadSem(String name, Counter l, SharedCounter ls, Semaphore sem,
                             long cDelay, long rDelay) {
        super(name);
        this.label = l;
        this.sharedLabel = ls;
        this.sem = sem;
        this.critDelay = cDelay;
        this.restDelay = rDelay;
        this.threadLoopCount = 0L;
        this.stopFlag = false;
    }

    public void terminate() {
        this.stopFlag = true;
    }

    public boolean isTerminated() {
        return stopFlag;
    }

    @Override
    public void run() {
        while (!stopFlag) {
            try {
                sem.acquire();
                sharedLabel.enter();
                delayFor(critDelay);
                sharedLabel.exit();
            } catch (InterruptedException e) {
            } finally {
                sem.release();
            }

            // end of critical code section
            
            // non-critical code can go here 
            label.setValue(++threadLoopCount);
            delayFor(restDelay);
        }
    }
    
    public void delayFor(long time){
        try {Thread.sleep(time);}
        catch (InterruptedException e) {System.out.println("?? in delayFor");}
    }
}
