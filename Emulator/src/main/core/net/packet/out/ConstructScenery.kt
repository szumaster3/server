package core.net.packet.out

import core.game.node.scenery.Scenery
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The outgoing packet for constructing scenery in the player's scene.
 *
 * @author Emperor
 */
class ConstructScenery : OutgoingPacket<OutgoingContext.BuildScenery> {
    override fun send(context: OutgoingContext.BuildScenery) {
        val player = context.player
        val o = context.scenery
        val buffer = write(UpdateAreaPosition.getBuffer(player, o.location.chunkBase), o)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        player.session.write(buffer)
    }

    companion object {
        /**
         * Writes the construct scenery data to the given buffer.
         *
         * @param buffer The buffer to write to.
         * @param object The scenery to construct.
         * @return The buffer with the written data.
         */
        @JvmStatic
        fun write(buffer: IoBuffer, `object`: Scenery): IoBuffer {
            val l = `object`.location
            buffer.put(179).putA((`object`.type shl 2) or (`object`.rotation and 0x3))
                .put((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7)).putShortA(`object`.id)
            return buffer
        }
    }
}
