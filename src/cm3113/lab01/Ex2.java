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
public class Ex2 {
    public static void main(String[] args) {
	MyThread t1 = new MyThread("t1");
	MyThread t2 = new MyThread("t2");
	MyThread t3 = new MyThread("t3");
	
	t1.start();
	t2.start();
	t3.start();
    }
}