package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The interface outgoing packet.
 *
 * @author Emperor
 */
class Interface : OutgoingPacket<OutgoingContext.InterfaceContext> {
    override fun send(context: OutgoingContext.InterfaceContext) {
        val buffer = IoBuffer(155)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        buffer.put(if (context.walkable) 1 else 0)
        buffer.putIntB(context.windowId shl 16 or context.componentId)
            .putShortA(context.player.interfaceManager.getPacketCount(1)).putShort(context.interfaceId)
        context.player.details.session.write(buffer)
    }
}