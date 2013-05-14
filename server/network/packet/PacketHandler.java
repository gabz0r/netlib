package server.network.packet;

import server.network.*;
import server.network.packet.enums.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 12.05.13
 * Time: 00:23
 * To change this template use File | Settings | File Templates.
 */
public class PacketHandler {
    public static void parse(Packet p, ClientHandler response) {
        switch(p.getOpcode()) {
            case Opcodes.IN.VERSION_CONTROL: {
                String msg = (String)p.read(DataTypes.EDT_STRING);
                if(msg.equals("version-control")) {
                    short clientVersion = (Short)p.read(DataTypes.EDT_SHORT);

                    System.out.println("Clientversion is OK! (" + clientVersion + ")");

                    Packet ok = new Packet(Opcodes.OUT.VERSION_OK, SecurityModes.ESM_PLAIN);
                    ok.write(DataTypes.EDT_SHORT, Server.currentClientVersion);
                    response.sendPacket(ok);
                }
                else {
                    break;
                }
                break;
            }

            case(Opcodes.IN.PUBLIC_KEY): {
                int keylen = (Integer)p.read(DataTypes.EDT_INT);
                System.out.println(keylen);
                Encryption.setOthersKey(p.readBYTES(keylen));

                System.out.println("Received public key from client, length: " + keylen);

                Packet publicKey = new Packet(Opcodes.OUT.PUBLIC_KEY, SecurityModes.ESM_PLAIN);
                publicKey.write(DataTypes.EDT_INT, Encryption.getPublicKey().getEncoded().length);
                publicKey.writeBYTES(Encryption.getPublicKey().getEncoded());

                System.out.println("Sending my public key, length: " + Encryption.getPublicKey().getEncoded().length);

                response.sendPacket(publicKey);
                break;
            }

            case(Opcodes.IN.PUBLIC_KEY_OK): {
                if(p.read(DataTypes.EDT_STRING).equals("public-ok")) {
                    Packet aesOut = new Packet(Opcodes.OUT.AES_KEY, SecurityModes.ESM_PLAIN);
                    Encryption.aesKey = Encryption.generateAESKey();
                    byte[] encKey = Encryption.encryptAESKey(Encryption.aesKey);

                    aesOut.write(DataTypes.EDT_INT, encKey.length);
                    aesOut.writeBYTES(encKey);

                    System.out.println("Client received public key, RSA exchange successful!");
                    System.out.println("Sending encrypted AES key, length: " + encKey.length);

                    response.sendPacket(aesOut);
                }
                break;
            }

            case(Opcodes.IN.ENCRYPTION_TEST): {
                if(p.read(DataTypes.EDT_STRING).equals("1234567890")) {
                    Packet handshakeOk = new Packet(Opcodes.OUT.HANDSHAKE_OK, SecurityModes.ESM_CRYPTED);
                    handshakeOk.write(DataTypes.EDT_STRING, "HANDSHAKE_OK_CONNECTION_OPEN");

                    response.sendPacket(handshakeOk);
                }
                break;
            }
        }

    }
}
