package core.net.event

import core.net.IoSession
import core.net.IoWriteEvent
import core.net.packet.IoBuffer
import core.net.packet.PacketHeader
import java.nio.ByteBuffer

/**
 * Handles game packet writing events.
 *
 * @author Emperor
 */
class GameWriteEvent(session: IoSession, context: Any) : IoWriteEvent(session, context) {

    override fun write(session: IoSession, context: Any) {
        when (context) {
            is ByteBuffer -> {
                session.queue(context)
                return
            }
            is IoBuffer -> {
                var buf = context.toByteBuffer().flip() as ByteBuffer?
                    ?: throw RuntimeException("Critical networking error: The byte buffer requested was null.")

                if (context.opcode() != -1) {
                    val packetLength = buf.remaining() + 4
                    val response = ByteBuffer.allocate(packetLength)
                    response.put(context.opcode().toByte())

                    when (context.header) {
                        PacketHeader.BYTE -> response.put(buf.remaining().toByte())
                        PacketHeader.SHORT -> response.putShort(buf.remaining().toShort())
                        else -> { /* do nothing */ }
                    }

                    response.put(buf)
                    response.flip()
                    buf = response
                }
                session.queue(buf)
            }
            else -> throw IllegalArgumentException("Unsupported context type: ${context::class}")
        }
    }
}
