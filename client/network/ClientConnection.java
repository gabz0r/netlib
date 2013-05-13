package client.network;

import client.Main;
import server.network.packet.*;
import server.network.packet.enums.DataTypes;
import server.network.packet.enums.SecurityModes;

import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 12.05.13
 * Time: 00:34
 * To change this template use File | Settings | File Templates.
 */
public class ClientConnection extends Thread {
    private Socket client;
    private byte[] buffer;

    private DataOutputStream outStream;
    private DataInputStream inStream;

    public ClientConnection(String ip, int port) {
        super();
        try {
            client = new Socket(ip,port);

            outStream = new DataOutputStream(this.client.getOutputStream());
            inStream = new DataInputStream(this.client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Packet hello = new Packet(0x0A, SecurityModes.ESM_PLAIN);
        hello.write(DataTypes.EDT_STRING, "Hallo Welt");
        sendPacket(hello);

        buffer = new byte[8192];
        try {
            int numBytes = inStream.read(buffer);
            byte[] resBuffer = new byte[numBytes];

            System.arraycopy(buffer,0,resBuffer,0,numBytes);

            Packet p = PacketBuilder.buildFromRaw(resBuffer);
            PacketHandler.parse(p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet out) {
        try {
            outStream.write(out.getPacket());
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
