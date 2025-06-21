package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the removal of the minimap flag.
 *
 * @author Emperor
 */
class ClearMinimapFlag : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val buffer = IoBuffer(153)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.details.session.write(buffer)
    }
}