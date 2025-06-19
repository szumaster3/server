package core.net.packet.out

import core.game.node.item.Item
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.BuildItemContext

/**
 * Sends information about the removed item to update the client view.
 *
 * @author Emperor
 */
class ClearGroundItem : OutgoingPacket<BuildItemContext> {
    override fun send(context: BuildItemContext) {
        val player = context.player
        val item = context.item
        val buffer = write(UpdateAreaPosition.getBuffer(player, item.location.chunkBase), item)
        buffer.cypherOpcode(player.session.isaacPair.output)
        player.session.write(buffer)
    }

    companion object {
        private const val CLEAR_GROUND_ITEM_OPCODE = 240

        /**
         * Writes the clear ground item packet data to the given buffer.
         *
         * @param buffer The buffer to write to.
         * @param item The item to be cleared.
         * @return The buffer with the written data.
         */
        @JvmStatic
        fun write(buffer: IoBuffer, item: Item): IoBuffer {
            val location = item.location
            buffer.put(CLEAR_GROUND_ITEM_OPCODE)
            buffer.putS((location.chunkOffsetX shl 4) or (location.chunkOffsetY and 0x7))
                .putShort(item.id)
            return buffer
        }
    }
}
