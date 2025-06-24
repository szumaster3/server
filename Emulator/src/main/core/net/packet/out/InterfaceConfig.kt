package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The outgoing interface configuration packet.
 *
 * @author Emperor
 */
class InterfaceConfig : OutgoingPacket<OutgoingContext.InterfaceConfigContext> {
    override fun send(context: OutgoingContext.InterfaceConfigContext) {
        val buffer = IoBuffer(21)
        buffer.putC(if (context.isHidden) 1 else 0)
        buffer.putShort(context.player.interfaceManager.getPacketCount(1))
        buffer.putLEInt(context.interfaceId shl 16 or context.childId)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.session.write(buffer)
    }
}
