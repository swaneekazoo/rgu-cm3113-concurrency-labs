package cm3113.lab05;

// File: Counter.java used in Case CM3113 Lab 5 Example 1
import javax.swing.*; 
import java.util.concurrent.atomic.AtomicLong;

public class Counter extends JLabel { 
  private AtomicLong theValue;
  
  public Counter() { 
    theValue = new AtomicLong(0L);
  }
  
  public void setValue(long value) { 
    theValue.set(value);
    this.setText("      " + theValue + "      ");
    repaint(); // unlike most swing methods, repaint is always thread-safe
  } 
} 

