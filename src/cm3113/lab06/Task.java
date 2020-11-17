package cm3113.lab06;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Task implements Runnable {
    public static final long MAX = 1000000000L ; private int id ;

    public Task(int id){
        this.id = id ;
    }

    @Override
    public void run() {
        DateFormat time = new SimpleDateFormat("HH:mm:ss:SSS") ;
        long start = System.currentTimeMillis() ; // get current time
        System.out.println("Starting task " + id + " at " + time.format(start));
        long sum = id ;
        for(long i=0;i<MAX;i++)
            sum=sum+3 ;
        System.out.println("task " + id +
                " sum = " + sum) ;
        long end = System.currentTimeMillis() ;
        System.out.println("Ending task " + id + " at " + time.format(end) +
                " after " + (end - start) + " milliseconds") ;
}
}