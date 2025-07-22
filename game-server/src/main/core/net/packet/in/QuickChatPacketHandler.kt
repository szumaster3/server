package core.net.packet.`in`

import core.api.log
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.net.packet.IncomingPacket
import core.net.packet.IoBuffer
import core.net.packet.QCRepository
import core.tools.Log

/**
 * Decodes the quick chat packet.
 *
 * Represents different varieties of the quick chat packet:
 *
 * - Standard: 3 (sometimes 4) bytes, no string replacements.
 * - Single-replacement: 5 bytes, includes one string replacement.
 *   The last 3 bytes can and should be converted to a [Short].
 * - Double-replacement: 7 bytes, includes two string replacements.
 *
 * Byte structure details:
 * 1. First byte: Indicates message target:
 *    - `0` = public chat
 *    - `1` = clan chat
 * 2. Second byte: Multiplier for 256 (values: 0, 1, 2, 3).
 * 3. Third byte: Index (can be negative).
 *    Added to (second byte × 256) to get the cache file ID of the quick chat message.
 *
 * In a single replacement packet, the following 3-byte short indicates either:
 * - The item ID, or
 * - The index of the selection in the menu.
 *
 * In a double replacement packet, the next 4 bytes look like: `0 1 0 3`, where:
 * - `0`s are spacers
 * - `1` and `3` are selection indexes for the menus.
 *
 * @author Ceikry
 */
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
