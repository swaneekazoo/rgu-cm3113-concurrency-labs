package cm3113.lab07.Ex1;

import java.util.ArrayList;
import java.util.Random;

/**
 * Producer class. 
 * Generates fixed number of random integers and saves in shared queue.
 */
public class Producer extends Thread{

    private static int count = 0, sum = 0;
    private Monitor queueMonitor;
    private Random rng;
    int number;
    
    public Producer(Monitor queueMonitor, int n){
        this.queueMonitor = queueMonitor;
        this.rng = new Random();
        this.number = n;
    }

    @Override public void run(){
        for(int i = 0; i <number; i++){
            int v = rng.nextInt(10);
            queueMonitor.add(v);
            count++;
            sum+=v;
        }
        System.out.println("Producer " + this.getId() + " finished");
    }
    
    public static void pauseFor(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {/* ignore */}
    }  
    
    public static int getCount(){
        return count;
    }
    
    public static int getTotal(){
        return sum;
    }
    
    public static void reset(){
        count = 0;
        sum = 0;
    }
}
