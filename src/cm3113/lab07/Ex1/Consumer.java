package cm3113.lab07.Ex1;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Consumer class. Reads data from shared queue, saved to shared data
 * Keeps track of number and sum of data processed in shared (static) variables
 */
public class Consumer extends Thread {
    private static int count = 0, sum = 0;
    CopyOnWriteArrayList<Integer> data;
    Monitor queueMonitor;
    Main gui;
    
    public Consumer(CopyOnWriteArrayList<Integer> data, Monitor queueMonitor, Main gui){
        this.data = data;
        this.queueMonitor = queueMonitor;
        this.gui = gui;
    }

    @Override public void run(){
        while(true){
            Integer v = queueMonitor.remove();
            sum = sum + v;
            data.add(v);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    count++;
                    gui.addToHistory(v+",");
                    if(count%20==0){
                        gui.addToHistory("\n");
                    }
                }
            });
            pauseFor(1);
        }
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
