/*
 * File: PCMonitorV1.java
 * CM3113 Lecture 09 Producer-Consumer solution using Monitor
 */
package lab7startingpoint.AlgorithmsDemo;
import java.awt.Color;

public class PCmonitorV1 implements PCmonitor{
    // 
    private Data[] theBuffer;
    private int theBufferSize;
    private int theIn, theOut;
    private int numOfItems;

    // constructor
    public PCmonitorV1(int size, Data[] data) {
        theBufferSize = size;
        theBuffer =data;
        theIn = 0;
        theOut = 0;
        numOfItems = 0;
    }

    public synchronized void add(Data t) {
        while (numOfItems == theBufferSize) {
            try { wait();
            } catch (InterruptedException e) {}
        }
        theBuffer[theIn] = t;
        theIn = (theIn + 1) % theBufferSize;
        numOfItems++;
        notifyAll();
    }

    public synchronized Data remove() {
        while (numOfItems == 0) {
            try { wait();
            } catch (InterruptedException e) {}
        }
        Data x = theBuffer[theOut];
        theBuffer[theOut] = new Data(".", Color.BLUE);
        theOut = (theOut + 1) % theBufferSize;
        numOfItems--;
        notifyAll();
        return x;
    }
}
