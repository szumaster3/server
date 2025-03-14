package core.net.packet.`in`

import core.api.log
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.net.packet.IncomingPacket
import core.net.packet.IoBuffer
import core.net.packet.QCRepository
import core.tools.Log

class QuickChatPacketHandler : IncomingPacket {
    override fun decode(
        player: Player?,
        opcode: Int,
        buffer: IoBuffer?,
    ) {
        buffer ?: return
        val x = buffer.toByteBuffer()

        val packetType =
            when (x.array().size) {
                3, 4 -> QCPacketType.STANDARD
                5 -> QCPacketType.SINGLE
                7 -> QCPacketType.DOUBLE
                else ->
                    QCPacketType.UNHANDLED.also {
                        log(this::class.java, Log.WARN, "UNHANDLED QC PACKET TYPE Size ${x.array().size}")
                    }
            }

        val forClan = (buffer.get() and 0xFF) == 1
        val multiplier: Int = buffer.get()
        val offset: Int = buffer.get()
        var selection_a_index = -1
        var selection_b_index = -1

        when (packetType) {
            QCPacketType.SINGLE -> {
                selection_a_index = buffer.short
            }

            QCPacketType.DOUBLE -> {
                buffer.get() // discard
                selection_a_index = buffer.get()
                buffer.get() // discard
                selection_b_index = buffer.get()
            }

            QCPacketType.UNHANDLED ->
                log(
                    this::class.java,
                    Log.WARN,
                    "Unhandled packet type, skipping remaining buffer contents.",
                )

            else -> {}
        }

        // Prints the values of each byte in the buffer to server log
        // If the world is in dev mode
        if (GameWorld.settings?.isDevMode == true) {
            log(this::class.java, Log.FINE, "Begin QuickChat Packet Buffer Dump---------")
            log(
                this::class.java,
                Log.FINE,
                "Packet Type: ${packetType.name} Chat Type: ${if (forClan) "Clan" else "Public"}",
            )
            x?.array()?.forEach {
                log(this::class.java, Log.FINE, "$it")
            }
            log(this::class.java, Log.FINE, "End QuickChat Packet Buffer Dump-----------")
        }

        QCRepository.sendQC(player, multiplier, offset, packetType, selection_a_index, selection_b_index, forClan)
    }
}

enum class QCPacketType {
    STANDARD,
    SINGLE,
    DOUBLE,
    UNHANDLED,
}
