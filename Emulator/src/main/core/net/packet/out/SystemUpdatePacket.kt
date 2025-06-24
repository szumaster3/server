package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the system update packet.
 *
 * @author Vexia
 */
class SystemUpdatePacket : OutgoingPacket<OutgoingContext.SystemUpdate> {
    override fun send(context: OutgoingContext.SystemUpdate) {
        val buffer = IoBuffer(85).putShort(context.time)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}
