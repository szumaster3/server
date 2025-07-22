package core.net.producer

import core.net.EventProducer
import core.net.IoReadEvent
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.event.HSReadEvent
import core.net.event.HSWriteEvent
import java.nio.ByteBuffer

/**
 * Produces I/O events for the handshake protocol.
 * @author Emperor
 */
class HSEventProducer : EventProducer {
    override fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent {
        return HSReadEvent(session, buffer)
    }

    override fun produceWriter(session: IoSession, context: Any): IoWriteEvent {
        return HSWriteEvent(session, context)
    }
}