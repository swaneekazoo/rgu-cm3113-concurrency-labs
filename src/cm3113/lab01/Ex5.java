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
public class Ex5 {
    static final int NUMTHREADS = 200;
    public static void main(String[] args) {
	
	System.out.println("This machine has "
+ java.lang.Runtime.getRuntime().availableProcessors() + " processors available");

	Thread[] threads = new Thread[NUMTHREADS];
	for(int i=0; i<NUMTHREADS; i++) {
	    threads[i] = new Thread(new Task(i));
	}

	threads[0].setPriority(1);
	threads[1].setPriority(10);

	for(int i=0; i<NUMTHREADS; i++) threads[i].start();
    }
}
