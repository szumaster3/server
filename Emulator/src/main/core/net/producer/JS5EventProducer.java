package core.net.producer;

import core.net.EventProducer;
import core.net.IoReadEvent;
import core.net.IoSession;
import core.net.IoWriteEvent;
import core.net.event.js5.JS5ReadEvent;
import core.net.event.js5.JS5WriteEvent;

import java.nio.ByteBuffer;

/**
 * The type Js 5 event producer.
 */
public class JS5EventProducer implements EventProducer {

    @Override
    public IoReadEvent produceReader(IoSession session, ByteBuffer buffer) {
        return new JS5ReadEvent(session, buffer);
    }

    @Override
    public IoWriteEvent produceWriter(IoSession session, Object context) {
        return new JS5WriteEvent(session, context);
    }

}
