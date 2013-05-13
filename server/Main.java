package server;

import server.network.Server;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 11.05.13
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        new Server().start();
    }

    public static boolean isServerRunning() {
        return true;
    }
}
