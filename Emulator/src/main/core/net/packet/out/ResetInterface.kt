package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Sends a reset interface packet to the client.
 *
 * @author Emperor
 */
class ResetInterface : OutgoingPacket<OutgoingContext.InterfaceContext> {
    override fun send(ic: OutgoingContext.InterfaceContext) {
        val buffer = IoBuffer(149)
        buffer.putShort(ic.player.interfaceManager.getPacketCount(1))
        buffer.putInt(ic.interfaceId)
        ic.player.session.write(buffer)
    }
}
