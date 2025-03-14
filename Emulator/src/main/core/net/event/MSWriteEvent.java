package core.net.event;

import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.packet.IoBuffer;

import java.nio.ByteBuffer;

/**
 * The type Ms write event.
 */
public final class MSWriteEvent extends IoWriteEvent {

    /**
     * Instantiates a new Ms write event.
     *
     * @param session the session
     * @param context the context
     */
    public MSWriteEvent(IoSession session, Object context) {
        super(session, context);
    }

    @Override
    public void write(IoSession session, Object context) {
        IoBuffer b = (IoBuffer) context;
        int size = b.toByteBuffer().position();
        ByteBuffer buffer = ByteBuffer.allocate(1 + size + b.getHeader().ordinal());
        buffer.put((byte) b.opcode());
        switch (b.getHeader()) {
            case NORMAL:
                break;
            case BYTE:
                buffer.put((byte) size);
                break;
            case SHORT:
                buffer.putShort((short) size);
                break;
        }
        buffer.put((ByteBuffer) b.toByteBuffer().flip());
        session.queue((ByteBuffer) buffer.flip());
    }

}