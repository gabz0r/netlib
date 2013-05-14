package client.network;

import client.Main;
import client.network.packet.enums.*;
import client.network.packet.*;
import client.network.packet.enums.*;

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
    public static short VERSION = 0x0001;

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
        System.out.println("Connected!");
        Packet version = new Packet(Opcodes.OUT.VERSION_CONTROL, SecurityModes.ESM_PLAIN);
        version.write(DataTypes.EDT_STRING, "version-control");
        version.write(DataTypes.EDT_SHORT, ClientConnection.VERSION);
        System.out.println("Sending version...");
        sendPacket(version);

        buffer = new byte[8192];
        while(true) {
        try {
                int numBytes = inStream.read(buffer);
                byte[] resBuffer = new byte[numBytes];

                System.arraycopy(buffer,0,resBuffer,0,numBytes);

                Packet p = PacketBuilder.buildFromRaw(resBuffer);
                PacketHandler.parse(p);

            } catch (Exception e) {
                break;
            }
        }
    }

    public void sendPacket(Packet out) {
        if(out.getSecurity() == SecurityModes.ESM_CRYPTED) {
            out = Encryption.encrypt(out);
        }
        try {
            outStream.write(out.getPacket());
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
