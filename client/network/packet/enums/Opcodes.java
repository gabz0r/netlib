package client.network.packet.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 14.05.13
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class Opcodes {
    public static class OUT {
        public static final short VERSION_CONTROL = 0x0001;
        public static final short PUBLIC_KEY = 0x0002;
        public static final short AES_CHECK = 0x0003;
        public static final short PUBLIC_KEY_OK = 0x0004;
        public static final short ENCRYPTION_TEST = 0x0005;
    }

    public static class IN {
        public static final short VERSION_OK = 0x1001;
        public static final short PUBLIC_KEY = 0x1002;
        public static final short AES_KEY = 0x1003;
        public static final short HANDSHAKE_OK = 0x1004;
    }
}
