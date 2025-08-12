package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import shared.consts.Network

/**
 * The outgoing set component string packet.
 *
 * @author Emperor
 */
class StringPacket : OutgoingPacket<OutgoingContext.StringContext> {
    override fun send(context: OutgoingContext.StringContext) {
        val buffer = IoBuffer(Network.SET_TEXT, PacketHeader.SHORT)
        buffer.putIntB((context.interfaceId shl 16) or context.lineId)
        buffer.putString(context.string)
        buffer.putShortA(context.player.interfaceManager.getPacketCount(1))
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}
