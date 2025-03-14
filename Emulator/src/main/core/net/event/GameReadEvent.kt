package core.net.event

import core.net.IoReadEvent
import core.net.IoSession
import core.net.packet.IoBuffer
import core.net.packet.PacketProcessor
import core.net.packet.`in`.Decoders530
import core.net.packet.`in`.Packet
import core.tools.Log
import java.nio.ByteBuffer
import core.api.log

class GameReadEvent(session: IoSession?, buffer: ByteBuffer) : IoReadEvent(session, buffer) {

    companion object {
        val PACKET_SIZES = intArrayOf(
            -3, -3, -3, 2, 2, -3, 8, -3, -3, 6, // 0-9
            4, -3, -3, -3, -3, -3, -3, 0, -3, -3, // 10-19
            4, 4, 1, 4, -3, -3, -3, 16, -3, -3, // 20-29
            2, -3, -3, 6, 8, -3, -3, -3, -3, -1, // 30-39
            -3, -3, -3, -3, -1, -3, -3, -3, 6, -3, // 40-49
            -3, -3, -3, 6, -3, 8, -3, 8, -3, -3, // 50-59
            -3, -3, -3, -3, 6, -1, 6, -3, 2, -3, // 60-69
            -3, 2, 2, 12, -3, 6, -3, -1, 2, 12, // 70-79
            -3, 8, 12, -3, 6, 8, -3, -3, -3, -3, // 80-89
            -3, -3, 2, 0, 2, -3, -3, -3, 4, 10, // 90-99
            -3, 14, -3, -3, 8, -3, 2, -3, -3, 6, // 100-109
            0, 2, -3, -3, 2, 10, -3, -1, -3, -3, // 110-119
            8, -3, -3, -1, 6, -3, -3, -3, -3, -3, // 120-129
            -3, 10, 6, 2, 14, 8, -3, 4, -3, -3, // 130-139
            -3, -3, -3, -3, -3, -3, -3, -3, 2, -3, // 140-149
            -3, -3, -3, 8, 8, 6, 8, 3, -3, -3, // 150-159
            -3, 8, 8, -3, -3, -3, 6, -1, 6, -3, // 160-169
            6, -3, -3, -3, -3, 2, -3, 2, -1, 4, // 170-179
            2, -3, -3, -3, 0, -3, -3, -3, 9, -3, // 180-189
            -3, -3, -3, -3, 6, 8, 6, -3, -3, 6, // 190-199
            -3, -1, -3, -3, -3, -3, 8, -3, -3, -3, // 200-209
            -3, -3, -3, 8, -3, -1, -3, -3, 2, -3, // 210-219
            -3, -3, -3, -3, -3, -3, -3, -3, 6, -3, // 220-229
            -3, 9, -3, 12, 6, -3, -3, -1, -3, 8, // 230-239
            -3, -3, -3, 6, 8, 0, -3, 6, 10, -3, // 240-249
            -3, -3, -3, 14, 6, -3 // 250-255
        )
    }

    override fun read(session: IoSession, buffer: ByteBuffer) {
        var last = -1
        while (buffer.hasRemaining()) {
            val opcode = buffer.get().toInt() and 0xFF
            val player = session.player ?: continue
            val header = PACKET_SIZES[opcode]
            var size = if (header < 0) getPacketSize(buffer, opcode, header, last) else header
            if (size == -1) {
                player.incrementInvalidPacketCount()
                break
            }
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
            val buf = IoBuffer(opcode, null, ByteBuffer.wrap(data))
            session.lastPing = System.currentTimeMillis()
            last = opcode

            if (player.opCounts[opcode]++ >= 10) {
                log(this.javaClass, Log.FINE, "Skipping packet $opcode because already received more than 10!")
                return
            }

            when (val processed = Decoders530.process(player, opcode, buf)) {
                is Packet.UnhandledOp -> log(this.javaClass, Log.WARN, "Unhandled opcode: $opcode")
                is Packet.DecodingError -> log(this.javaClass, Log.ERR, processed.message)
                !is Packet.NoProcess -> PacketProcessor.enqueue(processed)
                else -> {}
            }
        }
    }

    private fun getPacketSize(buffer: ByteBuffer, opcode: Int, header: Int, last: Int): Int {
        return when (header) {
            -1 -> if (buffer.remaining() < 1) {
                queueBuffer(opcode)
                -1
            } else buffer.get().toInt() and 0xFF

            -2 -> if (buffer.remaining() < 2) {
                queueBuffer(opcode)
                -1
            } else buffer.short.toInt() and 0xFFFF

            else -> -1
        }
    }
}
