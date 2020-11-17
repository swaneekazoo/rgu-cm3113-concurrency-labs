package cm3113.lab06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandlerRunnable implements Runnable {
    private Socket incoming;

    public ConnectionHandlerRunnable(Socket incoming) {
		System.out.println("New thread created");
        this.incoming = incoming;
    }

    @Override
    public void run() {
        try{
            handleConnection(incoming);
        } catch(IOException e) {
            System.out.println(e);
        }
    }

    private void handleConnection(Socket incoming) throws IOException {
        /* set up streams for bidirectional transfer across connection socket */
        BufferedReader in = new BufferedReader
                (new InputStreamReader(incoming.getInputStream())) ;
        PrintWriter out = new PrintWriter
                (incoming.getOutputStream(), true /* auto flush */) ;
        out.println("You are connected to: "
                + "\n\rHostname: " + incoming.getInetAddress().getHostName()
                + " IPAddress: " + incoming.getInetAddress().getHostAddress() + " On port " + incoming.getLocalPort() ) ;
        out.println("Type BYE to quit") ;
        boolean done = false ;
        while(!done) {
            String str = in.readLine();
            if (str == null) {
                done = true;
            } else {
                out.println("ECHO: " + str);
                if (str.trim().equals("BYE"))
                    done = true;
            }
        }
        incoming.close() ;
    }
}
