/*
 * File: RWMonitorV1.java
 * CM3003 Lecture 9 Example 5: Reader Writer solution using Monitor
 * This Reader-Writer solution gives Priority to Readers
 */
package cm3113.lab08;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RWmonitorV3 implements RWmonitor {    
    // 
    private int numReaders ;
    private int numWriters ;
    private int numWaitingNoReader ;
    private int numWaitingNoWriter ;
    private ReentrantLock lock;
    private Condition noReader;
    private Condition noWriter;

      // constructor
    public RWmonitorV3() {
        numReaders = 0;
        numWriters = 0;
        numReadersWaiting = 0;
        numWritersWaiting = 0;
		lock = new ReentrantLock();
		noReader = lock.newCondition();
		noWriter = lock.newCondition();
    }
  
    @Override
    public void startWrite() throws InterruptedException{
		try {
			lock.lock();
			while (numReaders > 0 ||
				numWriters > 0) {
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
			if (numWritersWaiting > 0) {
				okToWrite.signal();
			} else {
				okToRead.signal();
			}
		} finally {
			lock.unlock();
		}
    }
    
    @Override
    public void startRead() throws InterruptedException{
        try {
			lock.lock();
			while (numWriters > 0 || numWaitingNoReader > 0) {
				numReadersWaiting++;
				okToRead.await();
				numReadersWaiting--;
			}
			numReaders++;
			noWriter.signal();
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
