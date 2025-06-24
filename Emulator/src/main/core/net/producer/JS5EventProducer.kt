package core.net.producer

import core.net.EventProducer
import core.net.IoReadEvent
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.event.js5.JS5ReadEvent
import core.net.event.js5.JS5WriteEvent
import java.nio.ByteBuffer

/**
 * Produces JS-5 I/O events.
 * @author Tyler, Emperor
 */
class JS5EventProducer : EventProducer {
    override fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent {
        return JS5ReadEvent(session, buffer)
    }

    override fun produceWriter(session: IoSession, context: Any): IoWriteEvent {
        return JS5WriteEvent(session, context)
    }
}
