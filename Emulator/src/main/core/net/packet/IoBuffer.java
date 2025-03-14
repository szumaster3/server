package core.net.packet;

import core.cache.crypto.ISAACCipher;
import core.cache.misc.buffer.ByteBufferUtils;

import java.nio.ByteBuffer;

/**
 * The type Io buffer.
 */
public class IoBuffer {

    private static final int[] BIT_MASK = new int[32];

    static {
        for (int i = 0; i < 32; i++) {
            BIT_MASK[i] = (1 << i) - 1;
        }
    }

    private final PacketHeader header;
    private int packetSize;
    private int opcode;
    private ByteBuffer buf;
    private int bitPosition = 0;

    /**
     * Instantiates a new Io buffer.
     */
    public IoBuffer() {
        this(-1, PacketHeader.NORMAL, ByteBuffer.allocate(2048));
    }

    /**
     * Instantiates a new Io buffer.
     *
     * @param opcode the opcode
     */
    public IoBuffer(int opcode) {
        this(opcode, PacketHeader.NORMAL, ByteBuffer.allocate(2048));
    }

    /**
     * Instantiates a new Io buffer.
     *
     * @param opcode the opcode
     * @param header the header
     */
    public IoBuffer(int opcode, PacketHeader header) {
        this(opcode, header, ByteBuffer.allocate((1 << 16) + 1));
    }

    /**
     * Instantiates a new Io buffer.
     *
     * @param opcode the opcode
     * @param header the header
     * @param buf    the buf
     */
    public IoBuffer(int opcode, PacketHeader header, ByteBuffer buf) {
        this.opcode = opcode;
        this.header = header;
        this.buf = buf;
    }

    /**
     * Clear io buffer.
     *
     * @return the io buffer
     */
    public IoBuffer clear() {
        buf.clear();
        bitPosition = 0;
        return this;
    }

    /**
     * P 1 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p1(int value) {
        buf.put((byte) value);
        return this;
    }

    /**
     * P 1 add io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p1add(int value) {
        buf.put((byte) (value + 128));
        return this;
    }

    /**
     * P 1 sub io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p1sub(int value) {
        buf.put((byte) (128 - value));
        return this;
    }

    /**
     * P 1 neg io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p1neg(int value) {
        buf.put((byte) -value);
        return this;
    }

    /**
     * P 2 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p2(int value) {
        buf.put((byte) (value >> 8));
        buf.put((byte) value);
        return this;
    }

    /**
     * P 2 add io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p2add(int value) {
        buf.put((byte) (value >> 8));
        buf.put((byte) (value + 128));
        return this;
    }

    /**
     * Ip 2 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer ip2(int value) {
        buf.put((byte) value);
        buf.put((byte) (value >> 8));
        return this;
    }

    /**
     * Ip 2 add io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer ip2add(int value) {
        buf.put((byte) (value + 128));
        buf.put((byte) (value >> 8));
        return this;
    }

    /**
     * P 3 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p3(int value) {
        buf.put((byte) (value >> 16));
        buf.put((byte) (value >> 8));
        buf.put((byte) value);
        return this;
    }

    /**
     * Ip 3 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer ip3(int value) {
        buf.put((byte) value);
        buf.put((byte) (value >> 8));
        buf.put((byte) (value >> 16));
        return this;
    }

    /**
     * P 4 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p4(int value) {
        buf.put((byte) (value >> 24));
        buf.put((byte) (value >> 16));
        buf.put((byte) (value >> 8));
        buf.put((byte) value);
        return this;
    }

    /**
     * Ip 4 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer ip4(int value) {
        buf.put((byte) value);
        buf.put((byte) (value >> 8));
        buf.put((byte) (value >> 16));
        buf.put((byte) (value >> 24));
        return this;
    }

    /**
     * Mp 4 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer mp4(int value) {
        buf.put((byte) (value >> 16));
        buf.put((byte) (value >> 24));
        buf.put((byte) value);
        buf.put((byte) (value >> 8));
        return this;
    }

    /**
     * Imp 4 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer imp4(int value) {
        buf.put((byte) (value >> 8));
        buf.put((byte) value);
        buf.put((byte) (value >> 24));
        buf.put((byte) (value >> 16));
        return this;
    }

    /**
     * P 8 io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer p8(long value) {
        buf.put((byte) (value >> 56));
        buf.put((byte) (value >> 48));
        buf.put((byte) (value >> 40));
        buf.put((byte) (value >> 32));
        buf.put((byte) (value >> 24));
        buf.put((byte) (value >> 16));
        buf.put((byte) (value >> 8));
        buf.put((byte) value);
        return this;
    }

    /**
     * P var int io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer pVarInt(int value) {
        if ((value & 0xffffff80) != 0) {
            if ((value & 0xffffc000) != 0) {
                if ((value & 0xFFE00000) != 0) {
                    if ((value & 0xF0000000) != 0) {
                        this.p1(value >>> 28 | 0x80);
                    }
                    this.p1(value >>> 21 | 0x80);
                }
                this.p1(value >>> 14 | 0x80);
            }
            this.p1(value >>> 7 | 0x80);
        }
        return this.p1(value & 0x7F);
    }

    /**
     * P var long io buffer.
     *
     * @param size  the size
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer pVarLong(int size, long value) {
        int bytes = size - 1;
        if (bytes < 0 || bytes > 7)
            throw new IllegalArgumentException();
        for (int shift = bytes * 8; shift >= 0; shift -= 8)
            this.p1((byte) (value >> shift));
        return this;
    }

    /**
     * Psmarts io buffer.
     *
     * @param value the value
     * @return the io buffer
     */
    public IoBuffer psmarts(int value) {
        if (value >= 0 && value < 128)
            this.p1(value);
        else if (value >= 0 && value < 0x8000)
            this.p2(value + 0x8000);
        else
            throw new IllegalArgumentException("smart out of range: $value");
        return this;
    }

    /**
     * Psize io buffer.
     *
     * @param length the length
     * @return the io buffer
     */
    public IoBuffer psize(int length) {
        buf.put(buf.position() - length - 1, (byte) length);
        return this;
    }

    /**
     * Psizeadd io buffer.
     *
     * @param length the length
     * @return the io buffer
     */
    public IoBuffer psizeadd(int length) {
        buf.put(buf.position() - length - 1, (byte) (length + 128));
        return this;
    }

    /**
     * G 1 int.
     *
     * @return the int
     */
    public int g1() {
        return buf.get() & 0xFF;
    }

    /**
     * G 1 b int.
     *
     * @return the int
     */
    public int g1b() {
        return buf.get();
    }

    /**
     * G 1 add int.
     *
     * @return the int
     */
    public int g1add() {
        return (buf.get() - 128) & 0xFF;
    }

    /**
     * G 1 neg int.
     *
     * @return the int
     */
    public int g1neg() {
        return -(buf.get() & 0xFF);
    }

    /**
     * G 1 sub int.
     *
     * @return the int
     */
    public int g1sub() {
        return (128 - buf.get()) & 0xFF;
    }

    /**
     * G 2 int.
     *
     * @return the int
     */
    public int g2() {
        return ((buf.get() & 0xFF) << 8) + (buf.get() & 0xFF);
    }

    /**
     * G 2 add int.
     *
     * @return the int
     */
    public int g2add() {
        return ((buf.get() & 0xff) << 8) + ((buf.get() - 128) & 0xFF);
    }

    /**
     * G 2 b int.
     *
     * @return the int
     */
    public int g2b() {
        int value = ((buf.get() & 0xFF) << 8) + (buf.get() & 0xFF);
        if (value > 32767)
            value -= 0x10000;
        return value;
    }

    /**
     * Ig 2 int.
     *
     * @return the int
     */
    public int ig2() {
        return (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 8);
    }

    /**
     * Ig 2 add int.
     *
     * @return the int
     */
    public int ig2add() {
        return ((buf.get() - 128) & 0xFF) + ((buf.get() & 0xFF) << 8);
    }

    /**
     * G 3 int.
     *
     * @return the int
     */
    public int g3() {
        return ((buf.get() & 0xFF) << 16) + ((buf.get() & 0xFF) << 8) + (buf.get() & 0xFF);
    }

    /**
     * Ig 3 int.
     *
     * @return the int
     */
    public int ig3() {
        return (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 8) + ((buf.get() & 0xFF) << 16);
    }

    /**
     * G 4 int.
     *
     * @return the int
     */
    public int g4() {
        return ((buf.get() & 0xFF) << 24) + ((buf.get() & 0xFF) << 16) + ((buf.get() & 0xFF) << 8) + (buf.get() & 0xFF);
    }

    /**
     * Ig 4 int.
     *
     * @return the int
     */
    public int ig4() {
        return (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 8) + ((buf.get() & 0xFF) << 16) + ((buf.get() & 0xFF) << 24);
    }

    /**
     * M 4 int.
     *
     * @return the int
     */
    public int m4() {
        return ((buf.get() & 0xFF) << 16) + ((buf.get() & 0xFF) << 24) + (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 8);
    }

    /**
     * Im 4 int.
     *
     * @return the int
     */
    public int im4() {
        return ((buf.get() & 0xFF) << 8) + (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 24) + ((buf.get() & 0xFF) << 16);
    }

    /**
     * G 8 long.
     *
     * @return the long
     */
    public long g8() {
        long low = (long) this.g4() & 0xFFFFFFFFL;
        long high = (long) this.g4() & 0xFFFFFFFFL;
        return high + (low << 32);
    }

    /**
     * Put io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer put(int val) {
        buf.put((byte) val);
        return this;
    }

    /**
     * Put bytes io buffer.
     *
     * @param datas  the datas
     * @param offset the offset
     * @param len    the len
     * @return the io buffer
     */
    public IoBuffer putBytes(byte[] datas, int offset, int len) {
        for (int i = offset; i < len; i++) {
            put(datas[i]);
        }
        return this;
    }

    /**
     * Gets bytes.
     *
     * @param data the data
     * @param off  the off
     * @param len  the len
     */
    public final void getBytes(byte data[], int off, int len) {
        for (int k = off; k < len + off; k++) {
            data[k] = data[off++];
        }
    }

    /**
     * Put a io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putA(int val) {
        buf.put((byte) (val + 128));
        return this;
    }

    /**
     * Put c io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putC(int val) {
        buf.put((byte) -val);
        return this;
    }

    /**
     * Put s io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putS(int val) {
        buf.put((byte) (128 - val));
        return this;
    }

    /**
     * Put tri io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putTri(int val) {
        buf.put((byte) (val >> 16));
        buf.put((byte) (val >> 8));
        buf.put((byte) val);
        return this;
    }

    /**
     * Put short io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putShort(int val) {
        buf.putShort((short) val);
        return this;
    }

    /**
     * Put le short io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putLEShort(int val) {
        buf.put((byte) val);
        buf.put((byte) (val >> 8));
        return this;
    }

    /**
     * Put short a io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putShortA(int val) {
        buf.put((byte) (val >> 8));
        buf.put((byte) (val + 128));
        return this;
    }

    /**
     * Put le short a io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putLEShortA(int val) {
        buf.put((byte) (val + 128));
        buf.put((byte) (val >> 8));
        return this;
    }

    /**
     * Put int io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putInt(int val) {
        buf.putInt(val);
        return this;
    }

    /**
     * Put le int io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putLEInt(int val) {
        buf.put((byte) val);
        buf.put((byte) (val >> 8));
        buf.put((byte) (val >> 16));
        buf.put((byte) (val >> 24));
        return this;
    }

    /**
     * Put int a io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putIntA(int val) {
        buf.put((byte) (val >> 8));
        buf.put((byte) val);
        buf.put((byte) (val >> 24));
        buf.put((byte) (val >> 16));
        return this;
    }

    /**
     * Put int b io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putIntB(int val) {
        buf.put((byte) (val >> 16));
        buf.put((byte) (val >> 24));
        buf.put((byte) val);
        buf.put((byte) (val >> 8));
        return this;
    }

    /**
     * Put long io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putLong(long val) {
        buf.putLong(val);
        return this;
    }

    /**
     * Put smart io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putSmart(int val) {
        if (val > Byte.MAX_VALUE) {
            buf.putShort((short) (val + 32768));
        } else {
            buf.put((byte) val);
        }
        return this;
    }

    /**
     * Put int smart io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putIntSmart(int val) {
        if (val > Short.MAX_VALUE) {
            buf.putInt(val + 32768);
        } else {
            buf.putShort((short) val);
        }
        return this;
    }

    /**
     * Put string io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putString(String val) {
        buf.put(val.getBytes());
        buf.put((byte) 0);
        return this;
    }

    /**
     * Put jag string io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putJagString(String val) {
        buf.put((byte) 0);
        buf.put(val.getBytes());
        buf.put((byte) 0);
        return this;
    }

    /**
     * Put jag string 2 io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer putJagString2(String val) {
        byte[] packed = new byte[256];
        int length = ByteBufferUtils.packGJString2(0, packed, val);
        buf.put((byte) 0).put(packed, 0, length).put((byte) 0);
        return this;
    }

    /**
     * Put io buffer.
     *
     * @param val the val
     * @return the io buffer
     */
    public IoBuffer put(byte[] val) {
        buf.put(val);
        return this;
    }

    /**
     * Put reverse a.
     *
     * @param data   the data
     * @param start  the start
     * @param offset the offset
     */
    public void putReverseA(byte[] data, int start, int offset) {
        for (int i = offset + start; i >= start; i--) {
            putA(data[i]);
        }
    }

    /**
     * Put reverse.
     *
     * @param data   the data
     * @param start  the start
     * @param offset the offset
     */
    public void putReverse(byte[] data, int start, int offset) {
        for (int i = offset + start; i >= start; i--) {
            put(data[i]);
        }
    }

    /**
     * Put bits io buffer.
     *
     * @param numBits the num bits
     * @param value   the value
     * @return the io buffer
     */
    public IoBuffer putBits(int numBits, int value) {
        int bytePos = getBitPosition() >> 3;
        int bitOffset = 8 - (getBitPosition() & 7);
        bitPosition += numBits;
        for (; numBits > bitOffset; bitOffset = 8) {
            byte b = buf.get(bytePos);
            buf.put(bytePos, b &= ~BIT_MASK[bitOffset]);
            buf.put(bytePos++, b |= value >> numBits - bitOffset & BIT_MASK[bitOffset]);
            numBits -= bitOffset;
        }
        byte b = buf.get(bytePos);
        if (numBits == bitOffset) {
            buf.put(bytePos, b &= ~BIT_MASK[bitOffset]);
            buf.put(bytePos, b |= value & BIT_MASK[bitOffset]);
        } else {
            buf.put(bytePos, b &= ~(BIT_MASK[numBits] << bitOffset - numBits));
            buf.put(bytePos, b |= (value & BIT_MASK[numBits]) << bitOffset - numBits);
        }
        return this;
    }

    /**
     * Put io buffer.
     *
     * @param buffer the buffer
     * @return the io buffer
     */
    public IoBuffer put(IoBuffer buffer) {
        buffer.toByteBuffer().flip();
        buf.put(buffer.toByteBuffer());
        return this;
    }

    /**
     * Put a io buffer.
     *
     * @param buffer the buffer
     * @return the io buffer
     */
    public IoBuffer putA(IoBuffer buffer) {
        buffer.toByteBuffer().flip();
        while (buffer.toByteBuffer().hasRemaining()) {
            putA(buffer.toByteBuffer().get());
        }
        return this;
    }

    /**
     * Put io buffer.
     *
     * @param buffer the buffer
     * @return the io buffer
     */
    public IoBuffer put(ByteBuffer buffer) {
        buf.put(buffer);
        return this;
    }

    /**
     * Sets bit access.
     *
     * @return the bit access
     */
    public IoBuffer setBitAccess() {
        bitPosition = buf.position() * 8;
        return this;
    }

    /**
     * Sets byte access.
     *
     * @return the byte access
     */
    public IoBuffer setByteAccess() {
        buf.position((getBitPosition() + 7) / 8);
        return this;
    }

    /**
     * Get int.
     *
     * @return the int
     */
    public int get() {
        return buf.get();
    }

    /**
     * Gets a.
     *
     * @return the a
     */
    public int getA() {
        return (buf.get() & 0xFF) - 128;
    }

    /**
     * Gets c.
     *
     * @return the c
     */
    public int getC() {
        return -buf.get();
    }

    /**
     * Gets s.
     *
     * @return the s
     */
    public int getS() {
        return 128 - (buf.get() & 0xFF);
    }

    /**
     * Gets tri.
     *
     * @return the tri
     */
    public int getTri() {
        return ((buf.get() << 16) & 0xFF) | ((buf.get() << 8) & 0xFF) | (buf.get() & 0xFF);
    }

    /**
     * Gets short.
     *
     * @return the short
     */
    public int getShort() {
        return buf.getShort();
    }

    /**
     * Gets le short.
     *
     * @return the le short
     */
    public int getLEShort() {
        return (buf.get() & 0xFF) | ((buf.get() & 0xFF) << 8);
    }

    /**
     * Gets short a.
     *
     * @return the short a
     */
    public int getShortA() {
        return ((buf.get() & 0xFF) << 8) | (buf.get() - 128 & 0xFF);
    }

    /**
     * Gets le short a.
     *
     * @return the le short a
     */
    public int getLEShortA() {
        return (buf.get() - 128 & 0xFF) | ((buf.get() & 0xFF) << 8);
    }

    /**
     * Gets int.
     *
     * @return the int
     */
    public int getInt() {
        return buf.getInt();
    }

    /**
     * Gets le int.
     *
     * @return the le int
     */
    public int getLEInt() {
        return (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 8) + ((buf.get() & 0xFF) << 16) + ((buf.get() & 0xFF) << 24);
    }

    /**
     * Gets int a.
     *
     * @return the int a
     */
    public int getIntA() {
        return ((buf.get() & 0xFF) << 8) + (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 24) + ((buf.get() & 0xFF) << 16);
    }

    /**
     * Gets int b.
     *
     * @return the int b
     */
    public int getIntB() {
        return ((buf.get() & 0xFF) << 16) + ((buf.get() & 0xFF) << 24) + (buf.get() & 0xFF) + ((buf.get() & 0xFF) << 8);
    }

    /**
     * Gets long l.
     *
     * @return the long l
     */
    public long getLongL() {
        long first = getIntB();
        long second = getIntB();
        if (second < 0)
            second = second & 0xffffffffL;
        return (first << -41780448) + second;
    }

    /**
     * Gets long.
     *
     * @return the long
     */
    public long getLong() {
        return buf.getLong();
    }

    /**
     * Gets smart.
     *
     * @return the smart
     */
    public int getSmart() {
        int peek = buf.get(buf.position());
        if (peek <= (0xFF & peek)) {
            return buf.get() & 0xFF;
        }
        return (buf.getShort() & 0xFFFF) - 32768;
    }

    /**
     * Gets int smart.
     *
     * @return the int smart
     */
    public int getIntSmart() {
        int peek = buf.getShort(buf.position());
        if (peek <= Short.MAX_VALUE) {
            return buf.getShort() & 0xFFFF;
        }
        return (buf.getInt() & 0xFFFFFFFF) - 32768;
    }

    /**
     * Gets string.
     *
     * @return the string
     */
    public String getString() {
        return ByteBufferUtils.getString(buf);
    }

    /**
     * Gets jag string.
     *
     * @return the jag string
     */
    public String getJagString() {
        byte b = buf.get();
        if (b == 0) return "";
        return ((char) b) + ByteBufferUtils.getString(buf);
    }

    /**
     * Gets reverse a.
     *
     * @param is     the is
     * @param offset the offset
     * @param length the length
     * @return the reverse a
     */
    public IoBuffer getReverseA(byte[] is, int offset, int length) {
        for (int i = (offset + length - 1); i >= offset; i--) {
            is[i] = (byte) (buf.get() - 128);
        }
        return this;
    }

    /**
     * Cypher opcode.
     *
     * @param cipher the cipher
     */
    public void cypherOpcode(ISAACCipher cipher) {
        this.opcode += (byte) cipher.getNextValue();
    }

    /**
     * To byte buffer byte buffer.
     *
     * @return the byte buffer
     */
    public ByteBuffer toByteBuffer() {
        return buf;
    }

    /**
     * Opcode int.
     *
     * @return the int
     */
    public int opcode() {
        return opcode;
    }

    /**
     * Readable bytes int.
     *
     * @return the int
     */
    public int readableBytes() {
        return buf.capacity() - buf.remaining();
    }

    /**
     * Gets header.
     *
     * @return the header
     */
    public PacketHeader getHeader() {
        return header;
    }

    /**
     * Array byte [ ].
     *
     * @return the byte [ ]
     */
    public byte[] array() {
        return buf.array();
    }

    /**
     * Gets packet size.
     *
     * @return the packet size
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * Sets packet size.
     *
     * @param packetSize the packet size
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    /**
     * Gets bit position.
     *
     * @return the bit position
     */
    public int getBitPosition() {
        return bitPosition;
    }

}
