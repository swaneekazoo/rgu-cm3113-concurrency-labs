package lab7startingpoint.AlgorithmsDemo;

import java.awt.Color;

/**
 *
 * @author DAVID
 */
public class Data
{
    protected String value;
    protected Color color;
    static protected long numberWrites = 0, numberReads = 0;
    
    public Data(String d, Color c){
        value = d;
        color = c;        
    }
    
    public void write(String d){
        value = d;
        numberWrites++;
        color = Color.green;
    }
    
    public String read(){
        numberReads++;
        color = Color.blue;
        return value;       
    }
    
    @Override public String toString(){
        return "" + value;
    }
    
    public static void reset(){
        Data.numberWrites = 0;
        Data.numberReads = 0;
    }
}
