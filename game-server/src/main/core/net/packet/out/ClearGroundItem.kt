package core.net.packet.out

import core.game.node.item.Item
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Represents the outgoing packet of clearing ground items.
 *
 * @author Emperor
 */
class ClearGroundItem : OutgoingPacket<OutgoingContext.BuildItem> {
    override fun send(context: OutgoingContext.BuildItem) {
        val player = context.player
        val item = context.item
        val buffer = write(UpdateAreaPosition.getBuffer(player, item.location.chunkBase), item)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        player.session.write(buffer)
    }

    companion object {
        /**
         * Write io buffer.
         *
         * @param buffer the buffer
         * @param item   the item
         * @return the io buffer
         */
        @JvmStatic
        fun write(buffer: IoBuffer, item: Item): IoBuffer {
            val l = item.location
            buffer.put(240)
            buffer.putS((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7)).putShort(item.id)
            return buffer
        }
    }
}