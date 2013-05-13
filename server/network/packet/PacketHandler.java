package server.network.packet;

import server.network.packet.enums.DataTypes;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 12.05.13
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class PacketHandler {
    public static void parse(Packet p) {
        switch(p.getOpcode()) {
            case 0xA: {
                String msg = (String)p.read(DataTypes.EDT_STRING);
                System.out.println("Nachricht vom Client: " + msg);
            }
        }

    }
}
