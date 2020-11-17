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
public class AverageThread extends Thread {
    private ArrayCalculator calc;
    private double result;
    
    public AverageThread(ArrayCalculator c) {
	this.calc = c;
    }
    
    public void run() {
	result = 0.0;
	for (int i = 0; i < calc.getData().length; i++)
	    result += calc.getData()[i];
    }
    
    public double getResult() {
	return result;
    }
}
