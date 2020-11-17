package cm3113.lab01;

import java.time.LocalTime;

public class Scheduler implements Runnable {
    private long interval;
    private Thread schedulerThread;
    
    public Scheduler(long time) {
	interval = time;
	schedulerThread = new Thread(this);
	schedulerThread.setPriority(Thread.MAX_PRIORITY);
//	schedulerThread.setDaemon(true);
	schedulerThread.start();
    }
    
    @Override
    public void run() {
	while(true) {
	    try {
		System.out.println("Scheduler wakened at " + LocalTime.now()) ;
		Thread.sleep(interval) ;
	    } catch (InterruptedException ex) { /* ignore exception */}
	} 
    }
}
