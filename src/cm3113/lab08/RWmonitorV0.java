/*
 * File: RWMonitorV0.java
 * CM3003 Lecture 9 Example 5: Reader Writer solution using Monitor
 * This Reader-Writer solution gives Priority to Readers
 */
package cm3113.lab08;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWmonitorV0 implements RWmonitor {    
    // 
    private int numReaders ;
    private int numWriters ;
    private int numReadersWaiting ;
    private int numWritersWaiting ;

      // constructor
    public RWmonitorV0() {

        numReaders = 0;
        numWriters = 0;
        numReadersWaiting = 0;
        numWritersWaiting = 0;
    }
  
    @Override
    public void startWrite() throws InterruptedException{
        // does nothing
    }
    
    @Override
    public void endWrite(){
        // does nothing
    }    
    
    @Override
    public void startRead() throws InterruptedException{
        // does nothing
    }
    
    @Override
    public void endRead(){
        // does nothing
    }
   
}
