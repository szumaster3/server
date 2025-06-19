package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.PlayerContext

/**
 * Sends an update to the client's random.dat file, typically used for unique client identification.
 * This may include a generated UID and reserved values.
 *
 * @author Emperor
 */
class UpdateRandomFile : OutgoingPacket<PlayerContext> {
    override fun send(context: PlayerContext) {
        val buffer = IoBuffer(UPDATE_RANDOM_FILE_OPCODE)
        buffer.putInt(1) // Assumed UID value.
        buffer.put(0)
        buffer.put(0)
        buffer.put(0)
        buffer.put(0)
        repeat(4) { i ->
            buffer.putInt(i + 100)
        }
        context.player.session.write(buffer)
    }

    private companion object {
        private const val UPDATE_RANDOM_FILE_OPCODE = 211
    }
}
