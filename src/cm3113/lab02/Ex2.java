/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm3113.lab02;

/**
 *
 * @author adam
 */
public class Ex2 {
    public static final int SIZE = 100000000;
    public static final double RANGE = 1000.0;
    
    public static void main(String[] args) {
	double[] data = new double[SIZE] ; // and fill the array with random data
	for (int i = 0 ; i < SIZE ; i++) data[i] = Math.random() * RANGE;
	
	// let's go
	long startTime = System.currentTimeMillis();
	
	ArrayCalculator ac = new ArrayCalculator(data);
	
	MaxThread mx = new MaxThread(ac);
	MinThread mn = new MinThread(ac);
	AverageThread av = new AverageThread(ac);
	
	mx.start();
	mn.start();
	av.start();
		
	try {
	    mx.join();
	    mn.join();
	    av.join();
	} catch (InterruptedException e) {}
	
	// stop
	long endTime = System.currentTimeMillis() ;
	
	System.out.println("Calculation for " + SIZE + " values took " + (endTime - startTime) + " milliseconds");
	System.out.println("Max = " + mx.getResult() + "\nMin = " + mn.getResult() + "\nAverage = " + av.getResult());
    }
}
