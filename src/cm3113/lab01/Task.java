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
public class Task implements Runnable {
    public static final long MAX = 100000000L;
    private long id;
    
    public Task(long id) {
	this.id = id;
    }

    @Override
    public void run() {
	LocalTime start = LocalTime.now();
	System.out.println("Task " + id + " started at " + start);
	long sum = 0 ;
	for (long i = 0 ; i < MAX ; i++) { // this creates a time-consuming loop
	    sum++;
//	    if (id == 0) Thread.yield();
	}
	LocalTime finish = LocalTime.now();
	System.out.println("Task " + id + " ended at " + finish
	+ " after running for " + Duration.between(start,finish).toMillis() + " milliseconds, with sum = " + sum );
    }
}
