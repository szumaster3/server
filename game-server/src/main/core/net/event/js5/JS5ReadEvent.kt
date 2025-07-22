package core.net.event.js5

import core.net.*
import java.nio.ByteBuffer

class JS5ReadEvent(
    session: IoSession,
    buffer: ByteBuffer
) : IoReadEvent(session, buffer) {

    override fun read(session: IoSession, buffer: ByteBuffer) {
        while (buffer.hasRemaining()) {
            val opcode = buffer.g1()

            if (buffer.remaining() < 3) {
                queueBuffer(opcode)
                return
            }

            when (opcode) {
                PREFETCH_REQUEST -> {
                    val value = buffer.g3()
                    val index = value shr 16
                    val archive = value and 0xFFFF
                    session.write(Triple(index, archive, opcode == 0), true)
                }

                PRIORITY_REQUEST -> {
                    val value = buffer.g3()
                    val index = value shr 16
                    val archive = value and 0xFFFF
                    session.write(Triple(index, archive, opcode == 0))
                }

                STATUS_LOGGED_IN -> {
                    buffer.g3()
                }

                STATUS_LOGGED_OUT -> {
                    buffer.g3()
                }

                ENCRYPTED -> {
                    val value = buffer.g1()
                    val mark = buffer.g2()
                }

                ACKNOWLEDGE -> {
                    buffer.g3()
                }

                DISCONNECT -> {
                    buffer.g3()
                    session.disconnect()
                }

                else -> {
                }
            }
        }
    }

    companion object {
        const val PREFETCH_REQUEST = 0
        const val PRIORITY_REQUEST = 1
        const val STATUS_LOGGED_IN = 2
        const val STATUS_LOGGED_OUT = 3
        const val ENCRYPTED = 4
        const val ACKNOWLEDGE = 6
        const val DISCONNECT = 7
    }
}
