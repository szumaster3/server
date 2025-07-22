package core.net.packet.out

import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the ping packet sending.
 *
 * @author Emperor
 */
class PingPacket : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        // TODO: Find real ping packet, this is actually clear minimap flag
        // packet.
        // context.getPlayer().getDetails().getSession().write(new
        // IoBuffer(47));
    }
}