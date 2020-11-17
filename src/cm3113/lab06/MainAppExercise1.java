package cm3113.lab06;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainAppExercise1 {
    private static final int NTHREADS = 4;

    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NTHREADS);

        ServerSocket s = new ServerSocket(8189);
        for(;;) {
            Socket incoming = s.accept();
            ConnectionHandlerRunnable r = new ConnectionHandlerRunnable(incoming);
            pool.submit(new ConnectionHandlerRunnable(incoming));
        }
    }
}
