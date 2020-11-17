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
public class MaxThread extends Thread {
    private ArrayCalculator calc;
    private double result;
    
    public MaxThread(ArrayCalculator c) {
	this.calc = c;
    }
    
    @Override
    public void run() {
	result = calc.getData()[0] ;
	for (int i = 1 ; i < calc.getData().length ; i++)
	if (result < calc.getData()[i])
	    result = calc.getData()[i];
    }
    
    public double getResult() {
	return result;
    }
}
