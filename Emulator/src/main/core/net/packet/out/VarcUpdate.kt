package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Represents an outgoing packet responsible
 * for updating a's variable client-side.
 */
class VarcUpdate : OutgoingPacket<OutgoingContext.VarcUpdate> {

    /**
     * Sends a varc update to client.
     */
    override fun send(varcUpdateContext: OutgoingContext.VarcUpdate) {
        val player = varcUpdateContext.player
        if (varcUpdateContext.value <= 255) {
            val buffer = IoBuffer(65)
            buffer.putLEShort(player.interfaceManager.getPacketCount(1))
            buffer.putC(varcUpdateContext.value.toByte().toInt())
            buffer.putLEShortA(varcUpdateContext.varcId)
            player.session.write(buffer)
        } else {
            val buffer = IoBuffer(69)
            buffer.putLEShortA(player.interfaceManager.getPacketCount(1))
            buffer.putInt(varcUpdateContext.value)
            buffer.putShortA(varcUpdateContext.varcId)
            player.session.write(buffer)
        }
    }
}
