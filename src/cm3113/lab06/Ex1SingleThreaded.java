package cm3113.lab06;

import java.io.BufferedReader; import java.io.InputStreamReader; import java.io.PrintWriter; import java.net.ServerSocket; import java.net.Socket;
import java.io.IOException;
/* a non-threaded echo server */
public class Ex1SingleThreaded {
    public static void main(String[] args) throws IOException {
        /* listen for a connection request on server socket s */
        ServerSocket s = new ServerSocket(8189) ;
        for(;;){
            Socket incoming = s.accept() ; // incoming is the connection socket
            handleConnection(incoming); /* delegate handling to helper method */
        }
    }


    public static void handleConnection(Socket incoming) throws IOException {
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
