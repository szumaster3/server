package core.net.packet.out

import core.game.node.item.Item
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.BuildItemContext

/**
 * Sends old and new amounts for the given item to update display.
 *
 * @author Emperor
 */
class UpdateGroundItemAmount : OutgoingPacket<BuildItemContext> {
    override fun send(context: BuildItemContext) {
        val player = context.player
        val item = context.item
        val buffer = write(UpdateAreaPosition.getBuffer(player, item.location.chunkBase), item, context.oldAmount)
        buffer.cypherOpcode(player.session.isaacPair.output)
        player.session.write(buffer)
    }

    companion object {
        private const val UPDATE_GROUND_ITEM_AMOUNT_OPCODE = 14

        /**
         * Writes the update ground item amount packet to the buffer.
         *
         * @param buffer The buffer to write into.
         * @param item The item being updated.
         * @param oldAmount The previous amount of the item.
         * @return The buffer with packet data.
         */
        @JvmStatic
        fun write(buffer: IoBuffer, item: Item, oldAmount: Int): IoBuffer {
            val location = item.location
            buffer.put(UPDATE_GROUND_ITEM_AMOUNT_OPCODE)
                .put((location.chunkOffsetX shl 4) or (location.chunkOffsetY and 0x7))
                .putShort(item.id)
                .putShort(oldAmount)
                .putShort(item.amount)
            return buffer
        }
    }
}
