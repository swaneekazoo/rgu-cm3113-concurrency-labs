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
public class MaxCallable implements Callable<Double> {
    private ArrayCalculator calc;
    private double result;
    
    public MaxCallable(ArrayCalculator c) {
	this.calc = c;
    }

    @Override
    public Double call() {
	result = calc.getData()[0] ;
	for (int i = 1 ; i < calc.getData().length ; i++)
	if (result < calc.getData()[i])
	    result = calc.getData()[i];
	return result;
    }    
}
