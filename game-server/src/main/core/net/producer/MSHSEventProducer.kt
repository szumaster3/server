package core.net.producer

import core.net.EventProducer
import core.net.IoReadEvent
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.event.MSHSReadEvent
import core.net.event.MSHSWriteEvent
import java.nio.ByteBuffer

/**
 * Handles Management server events.
 * @author Emperor
 */
class MSHSEventProducer : EventProducer {
    override fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent {
        return MSHSReadEvent(session, buffer)
    }

    override fun produceWriter(session: IoSession, context: Any): IoWriteEvent {
        return MSHSWriteEvent(session, context)
    }
}