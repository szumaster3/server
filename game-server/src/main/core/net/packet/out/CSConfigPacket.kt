package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The outgoing packet for client script configs.
 *
 * @author Snickerize
 */
class CSConfigPacket : OutgoingPacket<OutgoingContext.CSConfig> {
    override fun send(context: OutgoingContext.CSConfig) {
        val buffer = IoBuffer(65)
        buffer.putLEShort(context.player.interfaceManager.getPacketCount(1))
        buffer.putC(context.value.toByte().toInt())
        buffer.putLEShortA(context.id)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}