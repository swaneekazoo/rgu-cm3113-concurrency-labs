/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm3113.lab01;

/**
 *
 * @author adam
 */
public class MyRunnable implements Runnable {
    // supply the public void run() method required by Runnable interface
    @Override public void run() {
	/* put code to be executed when a thread starts in the run() method.
	* This example prints the thread name and a count */
	String tname = Thread.currentThread().getName() ;
	int count = 0;
	for (;;) { // empty for loop creates an infinite loop, or use while(true)
	    System.out.println("This thread is " + tname + " " + count++);
//	    try {
//		Thread.sleep(100) ;
//	    } catch(InterruptedException ex) {
//		/* do nothing about the exception */
//	    }
	}
    }
}
