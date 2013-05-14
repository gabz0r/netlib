package server.network.packet;

import server.network.packet.enums.*;

import javax.crypto.*;
import java.security.*;
import java.security.spec.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 14.05.13
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
public class Encryption {
    private static KeyPairGenerator pairGen;
    private static KeyPair pair;
    private static PublicKey othersKey;

    private static Cipher aes;

    public static Key aesKey;

    static {
        try {
            pairGen  = KeyPairGenerator.getInstance("RSA");
            pairGen.initialize(2048);
            pair = pairGen.genKeyPair();
        } catch(NoSuchAlgorithmException e) { }
    }

    public static PublicKey getPublicKey() {
        return pair.getPublic();
    }

    public static PrivateKey getPrivateKey() {
        return pair.getPrivate();
    }

    public static void setOthersKey(byte[] key) {
        try {
            othersKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static PublicKey getOthersKey() {
        return othersKey;
    }

    public static SecretKey generateAESKey() {
        try {
            KeyGenerator aesGenerator = KeyGenerator.getInstance("AES");
            SecureRandom rand = new SecureRandom();
            aesGenerator.init(rand);

            return aesGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    public static byte[] encryptAESKey(Key aes) {
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.WRAP_MODE, getOthersKey());
            return c.wrap(aes);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    public static Packet decrypt(Packet in) {
        short len = in.getSize();
        byte[] packetData = in.readBYTES(len);

        try {
            aes = Cipher.getInstance("AES");
            aes.init(Cipher.DECRYPT_MODE, aesKey);
            byte[] plain = aes.doFinal(packetData);

            return new Packet(in.getOpcode(), SecurityModes.ESM_CRYPTED, plain);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    public static Packet encrypt(Packet in) {
        byte[] packetData = in.getData();

        try {
            aes = Cipher.getInstance("AES");
            aes.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] cipher = aes.doFinal(packetData);

            Packet ret =  new Packet(in.getOpcode(), SecurityModes.ESM_CRYPTED);
            ret.writeBYTES(cipher);

            return ret;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }
}
