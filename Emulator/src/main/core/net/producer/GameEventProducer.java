package core.net.producer;

import core.net.EventProducer;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.event.GameReadEvent;
import core.net.event.GameWriteEvent;

import java.nio.ByteBuffer;

/**
 * The type Game event producer.
 */
public final class GameEventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new GameReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new GameWriteEvent(session, context);
    }

}