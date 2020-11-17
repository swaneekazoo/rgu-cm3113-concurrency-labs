package cm3113.lab08;

import java.text.DecimalFormat;

/**
 *
 * @author dpl
 */
public class Reader<T> extends Thread  {
    Boolean go = true;
    long timeToRead, timeToConsume;
    RWmonitor monitor;
    T[] data;
    App frame;
    String name;
    static long readerWaits = 0L;
    static long readerNumber = 0L;
    DecimalFormat round = new DecimalFormat("0.000");
    
    Reader (long t1, long t2, RWmonitor m, T[] d, App f, String n){
        timeToRead = t1;
        timeToConsume = t2;
        monitor = m;
        data = d;
        //nData = data.length;
        frame = f;
        name = n;
        go = true;
    }
    
    @Override
    public void run(){
        while(go){
            try {
                long started = System.nanoTime();
                monitor.startRead();
                readerNumber++;
                readerWaits += System.nanoTime()-started; // time on queue
                
                Thread.sleep(timeToRead);
                int p = getPosition();
                String str = ((Data)data[p]).read();
                monitor.endRead();
                
                Thread.sleep(timeToConsume);
                System.out.println("R"+name+":"+p+":"+str);             
            } catch (InterruptedException ex) {}
            
        }
    }
    
    private int getPosition(){
       int a = (int)(data.length*Math.random());
       return a;
    }
    
    public static String getData(){
        if(readerNumber==0)return"";
        DecimalFormat round = new DecimalFormat("0.000");
        return "R:" + Reader.readerNumber 
                + " Average waiting time on queue = "
                + round.format(1.0*Reader.readerWaits/Reader.readerNumber/1000000)+"ms";
    }
    
    public static void reset(){
        readerNumber = 0L;
        readerWaits = 0L;
    }
    
    public void cease(){
        go = false;
    } 
}
