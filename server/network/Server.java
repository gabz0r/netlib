package server.network;

import server.Main;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 11.05.13
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
public class Server extends Thread {
    public static short currentClientVersion = 0x0001;

    private ServerSocket server;
    ExecutorService clientPool = Executors.newCachedThreadPool();

    @Override
    public void run() {
        try {
            server = new ServerSocket(22594);
            System.out.println("Server running on port 22594");

            while(Main.isServerRunning()) {
                Socket clientTemp = server.accept();
                Thread handler = new ClientHandler(clientTemp);
                clientPool.execute(handler);
                System.out.println("Incoming connection");
            }

        } catch (IOException e) {

        }
    }

}
