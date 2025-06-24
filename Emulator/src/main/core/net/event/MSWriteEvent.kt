package core.net.event

import core.net.IoSession
import core.net.IoWriteEvent
import core.net.packet.IoBuffer
import core.net.packet.PacketHeader
import java.nio.ByteBuffer

/**
 * Handles management server write events.
 *
 * @author Emperor
 */
class MSWriteEvent(
    session: IoSession,
    context: Any,
) : IoWriteEvent(session, context) {

    override fun write(session: IoSession, context: Any) {
        val b = context as IoBuffer
        val size = b.toByteBuffer().position()
        val header = b.header ?: PacketHeader.NORMAL
        val buffer = ByteBuffer.allocate(1 + size + header.ordinal)
        buffer.put(b.opcode().toByte())
        when (header) {
            PacketHeader.NORMAL -> {
            }
            PacketHeader.BYTE -> {
                buffer.put(size.toByte())
            }
            PacketHeader.SHORT -> {
                buffer.putShort(size.toShort())
            }
            else -> {
            }
        }
        buffer.put(b.toByteBuffer().flip() as ByteBuffer)
        buffer.flip()
        session.queue(buffer)
    }
}
