/*
 * 
 */
package cm3113.lab08;

public interface RWmonitor {
    public void startRead() throws InterruptedException;
    public void endRead() throws InterruptedException;
    public void startWrite() throws InterruptedException;
    public void endWrite() throws InterruptedException;
}
