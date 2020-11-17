package cm3113.lab03.synchronised;

/* File: Example2_unsynchronized.java    
 * Starting point CM31133 Lab3 Exercise 2 */
/**
 * This version of exercise 2 is unsynchronized. The version suffers from "lost
 * updates" to the shared static variable resource and used. 
 */
public class Example2_synchronized {

    public static void main(String[] args) {
        long NUMBER_RESOURCES = 100000000L;
        ResourceUser.setResources(NUMBER_RESOURCES);
        ResourceUser t1 = new ResourceUser("t1");
        ResourceUser t2 = new ResourceUser("t2");
        t1.start();
        t2.start();
		
        System.out.println("Total should be constant:" + NUMBER_RESOURCES);
        while (ResourceUser.getResourceLeft() > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println(ResourceUser.getReport()
            );
        }
    }
}

class ResourceUser extends Thread {
    private static long resource = 100000000L;
    private static long used = 0L;

    public ResourceUser(String name) {
        super(name);
    }

    public void run() {
        while (resource > 0) {
            takeResource();
            Thread.yield();  
            useResource();       
/* Use of yield has no effect on logic of the code, but it increase the non-safe
 * effects observed by introducing more context switches */
        }
    }

    public static synchronized void takeResource() {       
        resource--;
    }

    public static synchronized void useResource() {
        used++;
    }

    public static long getResourceLeft() {
        return resource;
    }

    public static long getResourceUsed() {
        return used;
    }

    public static synchronized String getReport() {
        return "Remaining = " + resource + "   Used = " + used + "  Total = " + (resource + used);
    }
    
    public static void setResources(long nResources){
        resource = nResources;
    }
}
