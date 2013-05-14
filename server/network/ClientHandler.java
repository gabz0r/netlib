package server.network;

import server.network.packet.*;
import server.network.packet.enums.*;

import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 11.05.13
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
public class ClientHandler extends Thread {
    private Socket client;
    private byte[] buffer;

    private DataOutputStream outStream;
    private DataInputStream inStream;

    public ClientHandler(Socket client) {
        super();
        this.client = client;

        try {
            outStream = new DataOutputStream(this.client.getOutputStream());
            inStream = new DataInputStream(this.client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        buffer = new byte[8192];
        while(true) {
            try {
                int numBytes = inStream.read(buffer);
                byte[] resBuffer = new byte[numBytes];

                System.arraycopy(buffer,0,resBuffer,0,numBytes);

                Packet p = PacketBuilder.buildFromRaw(resBuffer);
                PacketHandler.parse(p, this);

            } catch (Exception e) {
                return;
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
