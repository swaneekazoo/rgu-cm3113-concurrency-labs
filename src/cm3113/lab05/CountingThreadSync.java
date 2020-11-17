package cm3113.lab05;
/* File: CountingThread.java 
 * Starting Point to CM3113 Lab 5 Exercise 1 */
public class CountingThreadSync extends Thread {

    private final Counter label;
    private SharedCounter sharedLabel;
    private long threadLoopCount;
    private final long critDelay;
    private final long restDelay;
    private volatile boolean stopFlag;

    public CountingThreadSync(String name, Counter l, SharedCounter ls,
                              long cDelay, long rDelay) {
        super(name);
        this.label = l;
        this.sharedLabel = ls;
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
            synchronized (sharedLabel) {
                sharedLabel.enter();
                delayFor(critDelay);
                sharedLabel.exit();
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
