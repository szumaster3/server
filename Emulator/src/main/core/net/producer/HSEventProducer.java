package core.net.producer;

import core.net.EventProducer;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.event.HSReadEvent;
import core.net.event.HSWriteEvent;

import java.nio.ByteBuffer;

/**
 * The type Hs event producer.
 */
public final class HSEventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new HSReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new HSWriteEvent(session, context);
    }

}