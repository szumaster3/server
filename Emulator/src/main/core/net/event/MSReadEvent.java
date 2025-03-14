package core.net.event;

import core.net.IoReadEvent;
import core.net.IoSession;

import java.nio.ByteBuffer;

/**
 * The type Ms read event.
 */
public final class MSReadEvent extends IoReadEvent {

    private static final int[] PACKET_SIZE = {-1, -1, -1, -2, -1, -1, -2, -1, -2, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    /**
     * Instantiates a new Ms read event.
     *
     * @param session the session
     * @param buffer  the buffer
     */
    public MSReadEvent(IoSession session, ByteBuffer buffer) {
        super(session, buffer);
    }

    @Override
    public void read(IoSession session, ByteBuffer buffer) {
        int last = -1;
        while (buffer.hasRemaining()) {
            int opcode = buffer.get() & 0xFF;
            if (opcode >= PACKET_SIZE.length) {
                break;
            }
            int header = PACKET_SIZE[opcode];
            int size = header;
            if (header < 0) {
                size = getPacketSize(buffer, opcode, header, last);
            }
            if (size == -1) {
                break;
            }
            if (buffer.remaining() < size) {
                switch (header) {
                    case -2:
                        queueBuffer(opcode, size >> 8, size);
                        break;
                    case -1:
                        queueBuffer(opcode, size);
                        break;
                    default:
                        queueBuffer(opcode);
                        break;
                }
                break;
            }
            byte[] data = new byte[size];
            buffer.get(data);
            last = opcode;
        }
    }

    private int getPacketSize(ByteBuffer buffer, int opcode, int header, int last) {
        if (header == -1) {
            if (buffer.remaining() < 1) {
                queueBuffer(opcode);
                return -1;
            }
            return buffer.get() & 0xFF;
        }
        if (header == -2) {
            if (buffer.remaining() < 2) {
                queueBuffer(opcode);
                return -1;
            }
            return buffer.getShort() & 0xFFFF;
        }
        return -1;
    }

}
