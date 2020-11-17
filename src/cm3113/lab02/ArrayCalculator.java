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

public class ArrayCalculator {
    private double[] theData;
    
    public double[] getData() {
	return theData;
    }
    
    public ArrayCalculator(double[] data) {
	theData = data;
    }
    
    public double max() {
	double m = theData[0] ;
	for (int i = 1 ; i < theData.length ; i++)
	if (m < theData[i]) m = theData[i] ; return m ;
    }
    
    public double min() {
	double m = theData[0] ;
	for (int i = 1 ; i < theData.length ; i++)
	if (m > theData[i]) 
	    m = theData[i] ;
	return m ;
    }
    
    public double average() {
	double sum = 0.0;
	for (int i = 0; i < theData.length; i++)
	    sum += theData[i];
	return sum;
    }
}
