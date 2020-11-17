
/* File: Example3_unsynchronized_bank.java    
 * Starting point CM3113 Lab3 Exercise 3 */ 

package cm3113.lab03.synchronised;

/**
This version of exercise 3 is unsynchronized and suffers from the problem 
 * of lost updates. The effect of the lost updates can be noted from the 
 * shrinking total displayed by the test() method.
 * Note that the application finally deadlocks as all Transactions threads sleep.
 */
public class Example3_synchronized_bank  { 
  public static void main(String[] args) { 
    Bank b = new Bank(); 
    for (int i = 0; i < Bank.NACCOUNTS; i++) new Transactions(b,i).start(); 
   } 
} 
class Bank { 
  public static final int INITIAL_BALANCE = 1000; 
  public static final int NACCOUNTS = 10; 
  private long[] accounts; 
  private int ntransacts;

  public Bank() { 
    ntransacts = 0; 
         accounts = new long[NACCOUNTS]; 
    for (int i = 0; i < NACCOUNTS; i++)  accounts[i] = INITIAL_BALANCE;
    test(); 
  } 
  
  public void transfer(int from, int to, int amount) { 
    while (accounts[from] < amount) {
      try {  Thread.sleep(5); } 
      catch(InterruptedException e) {} 
    } 
    /* NB calls to yield() increase chances of unsafe operation */ 
	synchronized(this) {
		long temp = accounts[from] ; 
		if (Math.random() < 0.05) Thread.yield() ; 
		accounts[from] = temp - amount ; 
		temp = accounts[to] ; 
		if (Math.random() < 0.05) Thread.yield() ; 
		accounts[to] = temp + amount ; 
		ntransacts++; 
		if (ntransacts % 100 == 0) test(); 
	}
  } 
  
  public synchronized void test() { 
    long sum = 0; 
	for (int i = 0; i < NACCOUNTS; i++) sum += accounts[i]; 
		System.out.println("Transactions:" + ntransacts + " Sum: " + sum); 
	}
} 

class Transactions extends Thread  { 
  private Bank bank; 
  private int from; 
  public Transactions(Bank b, int i) { 
    from = i; 
    bank = b; 
  } 
  public void run() { 
    for (;;) { 
      int to = (int)(Bank.NACCOUNTS * Math.random()); 
      if (to == from) continue; 
      int amount = (int)(Bank.INITIAL_BALANCE * Math.random() * 0.1); 
      bank.transfer(from, to, amount); 
      try { sleep(1); } catch(InterruptedException e) {} 
    } 
  } 
} 


