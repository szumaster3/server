package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import shared.consts.Network

/**
 * The animate interface outgoing packet.
 *
 * @author Emperor
*/
class AnimateInterface : OutgoingPacket<OutgoingContext.AnimateInterface> {
    override fun send(context: OutgoingContext.AnimateInterface) {
        val buffer = IoBuffer(Network.ANIMATE_INTERFACE)
        buffer.putIntB((context.interfaceId shl 16) + context.childId)
        buffer.putLEShort(context.animationId)
        buffer.putShortA(context.player.interfaceManager.getPacketCount(1))
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}