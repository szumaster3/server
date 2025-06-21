package core.net.packet.out

import core.game.node.item.Item
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Updates the ground item amount.
 *
 * @author Emperor
 */
class UpdateGroundItemAmount : OutgoingPacket<OutgoingContext.BuildItem> {
    override fun send(context: OutgoingContext.BuildItem) {
        val player = context.player
        val item = context.item
        val buffer = write(UpdateAreaPosition.getBuffer(player, item.location.chunkBase), item, context.oldAmount)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        player.details.session.write(buffer)
    }

    companion object {
        /**
         * Writes the packet.
         * @param buffer The buffer.
         * @param item The item.
         */
        @JvmStatic
        fun write(buffer: IoBuffer, item: Item, oldAmount: Int): IoBuffer {
            val l = item.location
            buffer.put(14).put((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7)).putShort(item.id).putShort(oldAmount)
                .putShort(item.amount)
            return buffer
        }
    }
}