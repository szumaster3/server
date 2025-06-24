package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader

/**
 * Handles the interaction option changed outgoing packet.
 *
 * @author Emperor
 */
class InteractionOption : OutgoingPacket<OutgoingContext.InteractionOptionContext> {
    override fun send(context: OutgoingContext.InteractionOptionContext) {
        val buffer = IoBuffer(44, PacketHeader.BYTE)
        buffer.putLEShortA(-1)
        if (context.remove) {
            buffer.put(0)
        } else {
            buffer.put(if (context.index == 0) 1 else 0)
        }
        buffer.put(context.index + 1)
        buffer.putString(context.name)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.session.write(buffer)
    }
}