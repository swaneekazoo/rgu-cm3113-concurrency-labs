package cm3113.lab06;

/**
 * A basic client thread
 */
import java.io.*;
import java.net.*;

public class Client extends Thread {
    private int thePortNo;
    private boolean done = false;
    private String newBid;
    private ClientGUI gui;
    private Socket socket;
    private BufferedReader theInput;
    private PrintWriter theOutput;

    /* Constructs Client connecting to given port and communicates with ClientGUI*/
    public Client(int portNo, ClientGUI g) {
        thePortNo = portNo;
        gui = g;
    }

    /* Sends a message to the output PrintWriter*/
    public void sendMessageToServer(String s) {
        theOutput.println(s);
    }

    /* Secifies task for Client Thread to run */
    public void run() {
        try {
            // initialise Socket connection and associated reader and writer
            socket = new Socket("localhost", thePortNo);
            theInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            theOutput = new PrintWriter(socket.getOutputStream(), true /* auto flush */);
            theOutput.println("Connection from client: " + socket.toString());

            // loop that reads from input and sends update to GUI
            while (!done) {
                String message = theInput.readLine() ; 
                if (message == null) done = true ; 
                else {
                    String bits[] = message.split(",");// might be a useful thing to do
                    gui.updateText(message); 
                    if (message.trim().toUpperCase().equals("BYE")) done = true ; 
                }                 
            }          
        } catch (UnknownHostException e) {
            System.out.println("Oh no! Issue with creating a Socket via localhost to port " + thePortNo + "\n" + e.toString());
        } 
        catch (IOException e) {
            System.out.println("Oh no! Issue with either theInput or theOutput streams " + "\n" + e.toString());
        }
    }
}
