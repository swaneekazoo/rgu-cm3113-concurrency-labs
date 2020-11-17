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
public class MyThread extends Thread {
    public MyThread(String name) {
    super(name); // call Thread super-class constructor, passing thread name
    }
    
    /* override inherited Thread method run()
    * the method name run() is important here; whenever a thread starts
    * it expects to call a method with the signature public void run() */
    @Override
    public void run() {
	/* put code to be executed when this thread starts in the run() method.
	* This example just prints the thread name and a count. */
	String tname = Thread.currentThread().getName();
	int count = 0;
	for (;;) {
	    System.out.println("Thread name: " + tname + " count: " + count++);
	}
    }
}
