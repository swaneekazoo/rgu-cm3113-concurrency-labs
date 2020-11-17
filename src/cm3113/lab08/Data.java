package cm3113.lab08;

import java.awt.Color;

/**
 *
 * @author DAVID
 */
public class Data {
    protected String value;
    protected Color color;
    static protected long numberWrites = 0, numberReads = 0;
    
    public Data(String d){
        value = d;
        color = Color.LIGHT_GRAY;       
    }
    
    public void write(String d){
        value = d;
        numberWrites++;
        color = Color.green;
    }
    
    public String  read(){
        numberReads++;
        color = Color.blue;
        return value;       
    }
    
    @Override public String toString(){
        return ""+ value;
    }
    
    public static void resetCounts(){
        Data.numberWrites=0; 
        Data.numberReads=0;
    }

}
