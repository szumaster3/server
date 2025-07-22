package core.net.event

import core.net.IoReadEvent
import core.net.IoSession
import java.nio.ByteBuffer

/**
 * Handles reading Management server packets.
 *
 * @author Emperor
 */
class MSReadEvent(
    session: IoSession,
    buffer: ByteBuffer,
) : IoReadEvent(session, buffer) {

    companion object {
        private val PACKET_SIZE = intArrayOf(
            -1, -1, -1, -2, -1, -1, -2, -1, -2, -1,
            4, -1, -1, -1, -1, -1, -1, -1, -1, -1
        )
    }

    override fun read(session: IoSession, buffer: ByteBuffer) {
        var last = -1
        while (buffer.hasRemaining()) {
            val opcode = buffer.get().toInt() and 0xFF
            if (opcode >= PACKET_SIZE.size) break

            val header = PACKET_SIZE[opcode]
            var size = header
            if (header < 0) {
                size = getPacketSize(buffer, opcode, header, last)
            }
            if (size == -1) break

            if (buffer.remaining() < size) {
                when (header) {
                    -2 -> queueBuffer(opcode, size shr 8, size)
                    -1 -> queueBuffer(opcode, size)
                    else -> queueBuffer(opcode)
                }
                break
            }

            val data = ByteArray(size)
            buffer.get(data)
            last = opcode
        }
    }

    private fun getPacketSize(buffer: ByteBuffer, opcode: Int, header: Int, last: Int): Int {
        return when (header) {
            -1 -> {
                if (buffer.remaining() < 1) {
                    queueBuffer(opcode)
                    -1
                } else {
                    buffer.get().toInt() and 0xFF
                }
            }
            -2 -> {
                if (buffer.remaining() < 2) {
                    queueBuffer(opcode)
                    -1
                } else {
                    buffer.short.toInt() and 0xFFFF
                }
            }
            else -> -1
        }
    }
}
