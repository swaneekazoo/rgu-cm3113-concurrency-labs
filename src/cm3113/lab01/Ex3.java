/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm3113.lab01;

import java.time.Duration;
import java.time.LocalTime;

/**
 *
 * @author adam
 */
public class Ex3 {
    public static void main(String[] args) {
	LocalTime start = LocalTime.now();
	System.out.println("Main thread started at " + start);
	
	Scheduler scheduler = new Scheduler(10);
	    
	Task ts1 = new Task(0);
	Task ts2 = new Task(1);
	Task ts3 = new Task(2);
	
	Thread tr1 = new Thread(ts1);
	Thread tr2 = new Thread(ts2);
	Thread tr3 = new Thread(ts3);
	
//	System.out.println(Thread.MAX_PRIORITY);
//	System.out.println(Thread.MIN_PRIORITY);
//	System.out.println(Thread.NORM_PRIORITY);
//	
//	System.out.println(Thread.currentThread().getPriority());
//	System.out.println(tr1.getPriority());
//	System.out.println(tr2.getPriority());
//	System.out.println(tr3.getPriority());
	
	tr1.start();
	tr2.start();
	tr3.start();
	
	LocalTime finish = LocalTime.now();
	System.out.println("Main thread ended at " + finish
    + " after runing for " + Duration.between(start, finish).toMillis() + " milliseconds") ;
    }
}
