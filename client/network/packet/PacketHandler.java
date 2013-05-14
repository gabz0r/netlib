package client.network.packet;

import client.Main;
import client.network.packet.enums.*;

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
            case Opcodes.IN.VERSION_OK: {
                short currentVersion = (Short)p.read(DataTypes.EDT_SHORT);

                Packet publicKey = new Packet(Opcodes.OUT.PUBLIC_KEY, SecurityModes.ESM_PLAIN);
                publicKey.write(DataTypes.EDT_INT, Encryption.getPublicKey().getEncoded().length);
                publicKey.writeBYTES(Encryption.getPublicKey().getEncoded());
                System.out.println("Version OK! Sending public key, length: " + Encryption.getPublicKey().getEncoded().length);
                Main.con.sendPacket(publicKey);
                break;
            }

            case Opcodes.IN.PUBLIC_KEY: {
                int keylen = (Integer)p.read(DataTypes.EDT_INT);
                Encryption.setOthersKey(p.readBYTES(keylen));

                Packet gotIt = new Packet(Opcodes.OUT.PUBLIC_KEY_OK, SecurityModes.ESM_PLAIN);
                gotIt.write(DataTypes.EDT_STRING, "public-ok");

                System.out.println("Received public key from server, length: " + keylen);

                Main.con.sendPacket(gotIt);
                break;
            }

            case Opcodes.IN.AES_KEY: {
                int keylen = (Integer)p.read(DataTypes.EDT_INT);
                byte[] encKey = p.readBYTES(keylen);

                Encryption.aesKey = Encryption.decryptAESKey(encKey);
                System.out.println("Received server AES key, length: " + keylen);

                Packet encryptionTest = new Packet(Opcodes.OUT.ENCRYPTION_TEST, SecurityModes.ESM_CRYPTED);
                encryptionTest.write(DataTypes.EDT_STRING, "1234567890");

                Main.con.sendPacket(encryptionTest);
                break;
            }

            case Opcodes.IN.HANDSHAKE_OK: {
                if(p.read(DataTypes.EDT_STRING).equals("HANDSHAKE_OK_CONNECTION_OPEN")) {
                    System.out.println("Handshake done, connection is now safe!");
                }
            }
        }

    }
}
