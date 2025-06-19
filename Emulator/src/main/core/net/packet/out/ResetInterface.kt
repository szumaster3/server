package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.InterfaceContext

/**
 * Sends a reset interface packet to the client.
 *
 * @author Emperor
 */
class ResetInterface : OutgoingPacket<InterfaceContext> {
    override fun send(context: InterfaceContext) {
        val buffer = IoBuffer(PACKET_SIZE)
        buffer.putShort(context.player.interfaceManager.getPacketCount(1))
        buffer.putInt(context.interfaceId)
        context.player.session.write(buffer)
    }

    private companion object {
        private const val PACKET_SIZE = 149
    }
}