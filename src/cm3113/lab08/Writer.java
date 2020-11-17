package cm3113.lab08;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author dpl
 */
public class Writer<T> extends Thread {

    private long timeToProduce, timeToWrite;
    private RWmonitor monitor;
    private T[] data;
    //private int nData;
    private String name;
    boolean go = true;
    static long writerWaits;
    static long writerNumber;

    public Writer(long t1, long t2, RWmonitor m, T[] d, String n) {
        timeToProduce = t1;
        timeToWrite = t2;
        monitor = m;
        data = d;
        name = n;
        go = true;
    }

    @Override
    public void run() {
        while (go) {
            try {
                Thread.sleep(timeToProduce);
                int p = getPosition();
                String str = "" + (char) (Math.random() * 26 + 65);
                long started = System.nanoTime();
                monitor.startWrite();
                writerWaits += System.nanoTime()-started; // time on queue
                writerNumber++;
                                
                Thread.sleep(timeToWrite);
                ((Data) data[p]).write("W" + this.name + ":" + str);
                monitor.endWrite();
            } catch (InterruptedException ex) {
            }
        }

    }

    private int getPosition() {
        int a = (int) (data.length * Math.random());
        return a;
    }
    
    public static String getData(){
        if(writerNumber==0)return"";
        DecimalFormat round = new DecimalFormat("0.000");
        return "W:" + Writer.writerNumber 
                + " Average waiting tome on queue = "
                + round.format(1.0*writerWaits/writerNumber/1000000)+"ms";
    }
        
    public static void reset(){
        writerNumber = 0L;
        writerWaits = 0L;
    }
    
    public void cease(){
        go = false;
    }   
}
