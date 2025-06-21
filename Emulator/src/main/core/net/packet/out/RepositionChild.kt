package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the "reposition interface child" outgoing packet.
 *
 * @author Emperor
 */
class RepositionChild : OutgoingPacket<OutgoingContext.ChildPosition> {
    override fun send(context: OutgoingContext.ChildPosition) {
        val buffer = IoBuffer(119)
            .putShortA(context.player.interfaceManager.getPacketCount(1))
            .putLEInt(context.interfaceId shl 16 or context.childId)
            .putShort(context.position.x)
            .putShortA(context.position.y)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.session.write(buffer)
    }
}