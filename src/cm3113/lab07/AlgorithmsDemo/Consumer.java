package lab7startingpoint.AlgorithmsDemo;

import java.util.LinkedList;
import javax.swing.JLabel;

/**
 *
 * @author dpl
 */
public class Consumer<T> extends Thread  {
    Boolean go;
    volatile static long timePerConsume_ms;
    PCmonitor monitor;
    String name;
    volatile static long totalTimeInConsumers_ns = 0L;
    volatile static long totalTimeInMonitor_ns = 0L;
    volatile static long totalTimeWaiting_ns = 0L;
    volatile static long totalItemsConsumed = 0L;
    
    
    Consumer (PCmonitor m, String n, Boolean g){
        timePerConsume_ms = App.timeConsumerToReadData_ms + App.timeConsumerToConsumeData_ms;
        monitor = m;
        name = n;
        go = true;
        this.setDaemon(true);
    }
    
    @Override
    public void run(){
        while(go){
            try {
                long startedConsumer = System.nanoTime();
                Thread.sleep(App.timeConsumerToReadData_ms);
                long startedInMonitor = System.nanoTime();
                String str = monitor.remove().toString();
                Thread.sleep(App.timeConsumerToConsumeData_ms);
                totalTimeInMonitor_ns += (System.nanoTime()-startedInMonitor);                                         
                totalTimeInConsumers_ns += (System.nanoTime()-startedConsumer);
                totalTimeWaiting_ns = totalTimeInConsumers_ns - totalTimeInMonitor_ns;
                totalItemsConsumed++;
            } catch (InterruptedException ex) {}
            
        }
    }
    
    public static void reset(){
        totalItemsConsumed = 0L;
        totalTimeInConsumers_ns = 0L;
        totalTimeWaiting_ns = 0L;
        totalTimeInMonitor_ns = 0L;
    }
    
    public void cease(){
        go = false;
    }
}
