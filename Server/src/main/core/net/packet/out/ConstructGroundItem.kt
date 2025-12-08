package core.net.packet.out

import core.game.node.item.Item
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.BuildItemContext

/**
 * Represents the outgoing packet of constructing a ground item.
 * @author Emperor
 */
class ConstructGroundItem : OutgoingPacket<BuildItemContext> {
    override fun send(context: BuildItemContext) {
        val player = context.player
        val item = context.item
        val buffer = write(UpdateAreaPosition.getBuffer(player, item.location.chunkBase), item)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        player.details.session.write(buffer)
    }

    companion object {
        @JvmStatic
        fun write(buffer: IoBuffer, item: Item): IoBuffer {
            val l = item.location
            buffer.put(33)
            buffer.putLEShort(item.id).put((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7)).putShortA(item.amount)
            return buffer
        }
    }
}
