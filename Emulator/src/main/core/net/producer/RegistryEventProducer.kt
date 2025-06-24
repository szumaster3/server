package core.net.producer

import core.net.EventProducer
import core.net.IoReadEvent
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.event.RegistryReadEvent
import core.net.event.RegistryWriteEvent
import java.nio.ByteBuffer

/**
 * Handles world server registry.
 * @author Emperor
 */
class RegistryEventProducer : EventProducer {
    override fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent {
        return RegistryReadEvent(session, buffer)
    }

    override fun produceWriter(session: IoSession, context: Any): IoWriteEvent {
        return RegistryWriteEvent(session, context)
    }
}