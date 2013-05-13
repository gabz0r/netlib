package server.network.packet;

import server.network.packet.enums.*;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gabriel
 * Date: 11.05.13
 * Time: 23:07
 * To change this template use File | Settings | File Templates.
 */
public class Packet {
    private short opcode;
    private SecurityModes security;
    private short size;
    private byte[] data;

    private short pointer;

    private DataInputStream instream;
    private DataOutputStream outstream;
    private ByteArrayOutputStream bout;

    public Packet(int opcode, SecurityModes security, byte[] data) {
        this.opcode = (short)opcode;
        this.security = security;
        this.size = (short)data.length;
        this.data = data.clone();

        if(security == SecurityModes.ESM_CRYPTED) {
            //decrypt data
        }

        instream = new DataInputStream(new ByteArrayInputStream(data));
        pointer = 0;
    }

    public Packet(int opcode, SecurityModes security) {
        this.opcode = (short)opcode;
        this.security = security;

        bout = new ByteArrayOutputStream();
        outstream = new DataOutputStream(bout);
    }

    public byte[] readBYTES(int len) {
        byte[] dt = new byte[len];
        try {
            instream.read(dt, 0, len);
            pointer += len;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dt;
    }

    public Object read(DataTypes type) {
        switch(type) {
            case EDT_BYTE: {
                byte v = 0;
                try {
                    v =  instream.readByte();
                    pointer += 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }
            case EDT_SHORT: {
                short v = 0;
                try {
                    v =  instream.readShort();
                    pointer += 2;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }
            case EDT_INT: {
                int v = 0;
                try {
                    v =  instream.readInt();
                    pointer += 4;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }
            case EDT_FLOAT: {
                float v = 0;
                try {
                    v =  instream.readFloat();
                    pointer += 4;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }
            case EDT_DOUBLE: {
                double v = 0;
                try {
                    v =  instream.readDouble();
                    pointer += 8;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }
            case EDT_STRING: {
                String v = "";
                try {
                    v =  instream.readUTF();
                    pointer += v.length() * 2;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return v;
            }
            default: {
                return 0;
            }
        }
    }

    public void writeBYTES(byte[] dt) {
        try {
            outstream.write(dt);
            pointer += dt.length;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void write(DataTypes type, Object val) {
        switch(type) {
            case EDT_BYTE: {
                try {
                    outstream.writeByte((Byte)val);
                    pointer += 1;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case EDT_SHORT: {
                try {
                    outstream.writeShort((Short)val);
                    pointer += 2;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case EDT_INT: {
                try {
                    outstream.writeInt((Integer)val);
                    pointer += 4;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case EDT_FLOAT: {
                try {
                    outstream.writeFloat((Float)val);
                    pointer += 4;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case EDT_DOUBLE: {
                try {
                    outstream.writeDouble((Double)val);
                    pointer += 8;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case EDT_STRING: {
                try {
                    outstream.writeUTF((String)val);
                    pointer += ((String) val).length();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getPacket() {
        try {
            this.outstream.flush();

            byte[] finalized = new byte[2 + 1 + 2 + this.outstream.size()]; //opcode, security, payload, data
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(b);

            dos.writeShort(this.opcode);
            dos.writeByte((byte)(this.security == SecurityModes.ESM_CRYPTED ? 0x01 : 0x00));
            dos.writeShort(this.outstream.size());
            dos.write(this.bout.toByteArray());

            return b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getOpcode() {
        return opcode;
    }

    public short getSize() {
        return size;
    }

    public SecurityModes getSecurity() {
        return security;
    }
}