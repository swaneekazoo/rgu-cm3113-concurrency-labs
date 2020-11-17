/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm3113.lab02;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author adam
 */
public class Ex3 {
    public static final int SIZE = 100000000;
    public static final double RANGE = 1000.0;
    private static final int NUMTHREADS = 8;
    
    public static void main(String[] args) throws ExecutionException {
	double[] data = new double[SIZE] ; // and fill the array with random data
	for (int i = 0 ; i < SIZE ; i++) data[i] = Math.random() * RANGE;
	
	// let's go
	long startTime = System.currentTimeMillis();
	
	ArrayCalculator ac = new ArrayCalculator(data);
	
	ExecutorService executor = Executors.newFixedThreadPool(NUMTHREADS);
	
	ArrayList<Future<Double>> list = new ArrayList<>();
	
	Future<Double> mx = executor.submit(new MaxCallable(ac));
	Future<Double> mn = executor.submit(new MinCallable(ac));
	Future<Double> av = executor.submit(new AverageCallable(ac));
	executor.shutdown();
	
	try {
	    System.out.println("Max = " + mx.get() + "\nMin = " + mn.get() + "\nAverage = " + av.get());
	} catch (InterruptedException e) {}
		
	// stop
	long endTime = System.currentTimeMillis();
	
	System.out.println("Calculation for " + SIZE + " values took " + (endTime - startTime) + " milliseconds");

    }
}