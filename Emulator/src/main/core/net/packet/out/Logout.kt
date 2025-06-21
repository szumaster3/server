package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The outgoing logout packet.
 *
 * @author Emperor
 */
class Logout : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val buffer = IoBuffer(86)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.details.session.write(buffer)
    }
}