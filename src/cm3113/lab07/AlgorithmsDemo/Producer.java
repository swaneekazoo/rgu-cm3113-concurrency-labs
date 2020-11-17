package lab7startingpoint.AlgorithmsDemo;

import java.awt.Color;

/**
 *
 * @author dpl
 */
public class Producer extends Thread {

    volatile public static long timePerProduct_ms;
    PCmonitor monitor;
    private String name;
    volatile Boolean go;
    volatile static long totalTimeInProducers_ns;
    volatile static long totalTimeInMonitor_ns;
    volatile static long totalTimeWaiting_ns;
    volatile static long totalItemsProduced;
    
    

    public Producer(PCmonitor m, String n, Boolean g) {
        timePerProduct_ms = App.timeProducerToProduceData_ms + App.timeProducerToWriteData_ms;
        monitor = m;
        name = n;
        go = true;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (go) {
            try {
                long startedProducing = System.nanoTime();
                Thread.sleep(App.timeProducerToProduceData_ms);
                String str = "" + (char) (Math.random() * 26 + 65);
                //System.out.println("P:"+name+":"+str);
                long startedInMonitor = System.nanoTime();
                monitor.add(new Data(str,new Color(0,(int)(128+Math.random()*128),0)));
                Thread.sleep(App.timeProducerToWriteData_ms);
                totalTimeInMonitor_ns += (System.nanoTime()-startedInMonitor);
                totalTimeInProducers_ns += (System.nanoTime()-startedProducing);
                totalTimeWaiting_ns = totalTimeInProducers_ns - totalTimeInMonitor_ns;
                totalItemsProduced++;
                
            } catch (InterruptedException ex) {/* ignore */}
        }

    }
        
    public static void reset(){
        totalItemsProduced = 0L;
        totalTimeInProducers_ns = 0L;
        totalTimeWaiting_ns = 0L;
        totalTimeInMonitor_ns = 0L;
    }
    
    public void cease(){
        go = false;
    }
    
}
