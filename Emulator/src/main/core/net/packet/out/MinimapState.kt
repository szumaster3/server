package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the sending of the minimap state outgoing packet.
 *
 * @author Emperor
 */
class MinimapState : OutgoingPacket<OutgoingContext.MinimapState> {
    override fun send(context: OutgoingContext.MinimapState) {
        val buffer = IoBuffer(192).put(context.state)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.details.session.write(buffer)
    }
}