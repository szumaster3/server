package core.net.producer

import core.net.EventProducer
import core.net.IoReadEvent
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.event.GameReadEvent
import core.net.event.GameWriteEvent
import java.nio.ByteBuffer

/**
 * Produces game packet I/O events.
 * @author Emperor
 */
class GameEventProducer : EventProducer {
    override fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent {
        return GameReadEvent(session, buffer)
    }

    override fun produceWriter(session: IoSession, context: Any): IoWriteEvent {
        return GameWriteEvent(session, context)
    }
}