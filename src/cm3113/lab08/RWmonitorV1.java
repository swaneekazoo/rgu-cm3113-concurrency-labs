/*
 * File: RWMonitorV1.java
 * CM3003 Lecture 9 Example 5: Reader Writer solution using Monitor
 * This Reader-Writer solution gives Priority to Readers
 */
package cm3113.lab08;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWmonitorV1 implements RWmonitor {    
    // 
    private int numReaders ;
    private int numWriters ;
    private int numReadersWaiting ;
    private int numWritersWaiting ;
    private ReentrantLock lock;
    private Condition okToRead;
    private Condition okToWrite;

      // constructor
    public RWmonitorV1() {
        numReaders = 0;
        numWriters = 0;
        numReadersWaiting = 0;
        numWritersWaiting = 0;
		lock = new ReentrantLock();
		okToRead = lock.newCondition();
		okToWrite = lock.newCondition();
    }
  
    @Override
    public void startWrite() throws InterruptedException{
		try {
			lock.lock();
			while (numReaders > 0 ||
				numWriters > 0 ||
				numReadersWaiting > 0) {
			numWritersWaiting++;
			okToWrite.await();
			numWritersWaiting--;
		}
		numWriters++;
		} finally {
			lock.unlock();
		}
    }
	
    @Override
    public void endWrite(){
		try {
			lock.lock();
			numWriters--;
			if (numReadersWaiting > 0) {
				okToRead.signal();
			} else {
				okToWrite.signal();
			}
		} finally {
			lock.unlock();
		}
    }
    
    @Override
    public void startRead() throws InterruptedException{
        try {
			lock.lock();
			while (numWriters > 0) {
				numReadersWaiting++;
				okToRead.await();
				numReadersWaiting--;
			}
			numReaders++;
			okToRead.signal();
		} finally {
			lock.unlock();
		}
    }
    
    @Override
    public void endRead(){
        try {
			lock.lock();
			numReaders--;
			if (numReaders == 0) {
				okToWrite.signal();
			}
		} finally {
			lock.unlock();
		}
    }
}
