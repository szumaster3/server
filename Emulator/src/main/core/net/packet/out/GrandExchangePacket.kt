package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader

/**
 * The outgoing packet used for updating a player's grand exchange data.
 *
 * @author Emperor, Vexia, Angle
 */
class GrandExchangePacket : OutgoingPacket<OutgoingContext.GrandExchange> {

    private val REMOVED = 6
    private val ABORTED = 2

    override fun send(context: OutgoingContext.GrandExchange) {
        val buffer = IoBuffer(116, PacketHeader.NORMAL)
        buffer.put(context.idx.toInt())

        if (context.state.toInt() == REMOVED) {
            buffer.put(0)
                .putShort(0)
                .putInt(0)
                .putInt(0)
                .putInt(0)
                .putInt(0)
        } else {
            var state = (context.state + 1).toByte()
            if (context.isSell) {
                state = (state.toInt() + 8).toByte()
            }
            if (context.state.toInt() == ABORTED) {
                state = if (context.isSell) (-3).toByte() else 5.toByte()
            }
            buffer.put(state.toInt())
                .put(context.itemID.toInt())
                .putInt(context.value)
                .putInt(context.amt)
                .putInt(context.completedAmt)
                .putInt(context.totalCoinsExchanged)
        }
        try {
            buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
            context.player.session.write(buffer)
        } catch (e: Exception) {

        }
    }
}
