/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm3113.lab02;

import java.util.concurrent.Callable;

/**
 *
 * @author adam
 */
public class AverageCallable implements Callable<Double> {
    private ArrayCalculator calc;
    private double result;
    
    public AverageCallable(ArrayCalculator c) {
	this.calc = c;
    }

    @Override
    public Double call() {
	result = 0.0;
	for (int i = 0; i < calc.getData().length; i++)
	    result += calc.getData()[i];
	return result;
    }
}
