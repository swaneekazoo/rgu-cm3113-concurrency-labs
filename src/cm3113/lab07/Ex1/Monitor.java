package cm3113.lab07.Ex1;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int[] buffer;
    private int bufferSize, in, out, numItems;

    private ReentrantLock lock;
    private Condition bufferNotEmpty, bufferNotFull;

    public Monitor(int bufferSize) {
        this.bufferSize = bufferSize;
        buffer = new int[bufferSize];
        in = 0;
        out = 0;
        numItems = 0;
        lock = new ReentrantLock();
        bufferNotEmpty = lock.newCondition();
        bufferNotFull = lock.newCondition();
    }
    
    public void add(int data) {
        try{
            lock.lock();
            while(numItems == bufferSize) {
                try{
                    bufferNotFull.await();
                } catch (InterruptedException e) {}
            }
            buffer[in] = data;
            in = (++in) % bufferSize;
            numItems++;
            bufferNotEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Integer remove() {
        int data;
        try{
            lock.lock();
            while(numItems == 0) {
                try{
                    bufferNotEmpty.await();
                } catch (InterruptedException e) {}
            }
            data =
            out = (++out) % bufferSize;
            numItems--;
            bufferNotFull.signal();
        } finally {
            lock.unlock();
        }
        return data;
    }

    public int getQueueLength() {
        return lock.getQueueLength();
    }
}
