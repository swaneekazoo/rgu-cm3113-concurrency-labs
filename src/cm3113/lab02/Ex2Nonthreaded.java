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
public class Ex2Nonthreaded {
    public static final int SIZE = 100000000 ;
    public static final double RANGE = 1000.0 ;
    
    public static void main(String[] args) {
	double[] data = new double[SIZE] ; // and fill the array with random data
	for (int i = 0 ; i < SIZE ; i++) data[i] = Math.random() * RANGE ;
	
	long startTime = System.currentTimeMillis() ;
	ArrayCalculator ac = new ArrayCalculator(data) ;
	double mx = ac.max() ;
	double mn = ac.min() ;
	double av = ac.average() ;
	
	long endTime = System.currentTimeMillis() ;
	
	System.out.println("Calculation for " + SIZE + " values took " + (endTime - startTime) + " milliseconds") ;
	System.out.println("Max = " + mx + "\nMin = " + mn + "\nAverage = " + av) ;
    }
}
