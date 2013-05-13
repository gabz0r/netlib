package client;

import client.network.ClientConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 11.05.13
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static ClientConnection con;

    public static void main(String[] args) {
        con = new ClientConnection("127.0.0.1", 22594);
        con.start();
    }
}
