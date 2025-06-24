package core.net.producer

import core.net.EventProducer
import core.net.IoReadEvent
import core.net.IoSession
import core.net.IoWriteEvent
import core.net.event.LoginReadEvent
import core.net.event.LoginWriteEvent
import java.nio.ByteBuffer

/**
 * Produces login I/O events.
 * @author Emperor
 */
class LoginEventProducer : EventProducer {
    override fun produceReader(session: IoSession, buffer: ByteBuffer): IoReadEvent {
        return LoginReadEvent(session, buffer)
    }

    override fun produceWriter(session: IoSession, context: Any): IoWriteEvent {
        return LoginWriteEvent(session, context)
    }
}