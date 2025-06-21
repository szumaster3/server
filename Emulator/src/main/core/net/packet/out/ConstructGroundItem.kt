package core.net.packet.out

import core.game.node.item.Item
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Represents the outgoing packet of constructing a ground item.
 *
 * @author Emperor
 */
class ConstructGroundItem : OutgoingPacket<OutgoingContext.BuildItem> {
    override fun send(context: OutgoingContext.BuildItem) {
        val player = context.player
        val item = context.item
        val buffer = write(UpdateAreaPosition.getBuffer(player, item.location.chunkBase), item)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        player.details.session.write(buffer)
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
            buffer.put(33)
            buffer.putLEShort(item.id).put((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7)).putShortA(item.amount)
            return buffer
        }
    }
}
