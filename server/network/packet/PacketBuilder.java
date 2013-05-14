package server.network.packet;

import server.network.packet.enums.*;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 11.05.13
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public class PacketBuilder {

    public static Packet buildFromRaw(byte[] data) throws Exception {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

        short opcode = dis.readShort();
        SecurityModes security = (dis.readByte() == 0x01 ? SecurityModes.ESM_CRYPTED : SecurityModes.ESM_PLAIN);
        short payload = dis.readShort();

        byte[] dt = new byte[payload];

        dis.read(dt, 0, payload);

        Packet ret = new Packet(opcode, security, dt);
        if(security == SecurityModes.ESM_CRYPTED) {
            ret = Encryption.decrypt(ret);
        }

        return ret;
    }
}
