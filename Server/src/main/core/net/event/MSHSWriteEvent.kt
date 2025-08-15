package core.net.event

import core.ServerConstants
import core.cache.util.ByteBufferExtensions
import core.net.IoSession
import core.net.IoWriteEvent
import java.nio.ByteBuffer

/**
 * Handles the management server handshake write event.
 *
 * @author Emperor
 */
class MSHSWriteEvent(session: IoSession, context: Any) : IoWriteEvent(session, context) {

    override fun write(session: IoSession, context: Any) {
        val buffer = ByteBuffer.allocate(2 + ServerConstants.MS_SECRET_KEY.length)
        buffer.put(88.toByte())
        ByteBufferExtensions.putString(ServerConstants.MS_SECRET_KEY, buffer)
        buffer.flip()
        session.queue(buffer)
    }
}
