package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the outgoing weight update packet.
 * @author Emperor
 */
class WeightUpdate : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val buffer = IoBuffer(174)
        buffer.putShort(context.player.settings.weight.toInt())
        // TODO context.getPlayer().getSession().write(buffer);
    }
}