package core.net.packet.out

import core.game.node.scenery.Scenery
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.BuildSceneryContext

/**
 * The outgoing packet for constructing scenery in the player's scene.
 * Sends information about a newly built scenery object.
 *
 * @author Emperor
 */
class ConstructScenery : OutgoingPacket<BuildSceneryContext> {
    override fun send(context: BuildSceneryContext) {
        val player = context.player
        val scenery = context.scenery
        val buffer = write(UpdateAreaPosition.getBuffer(player, scenery.location.chunkBase), scenery)
        buffer.cypherOpcode(player.session.isaacPair.output)
        player.session.write(buffer)
    }

    companion object {
        private const val CONSTRUCT_SCENERY_OPCODE = 179

        /**
         * Writes the construct scenery data to the given buffer.
         *
         * @param buffer The buffer to write to.
         * @param scenery The scenery to construct.
         * @return The buffer with the written data.
         */
        @JvmStatic
        fun write(buffer: IoBuffer, scenery: Scenery): IoBuffer {
            val location = scenery.location
            buffer.put(CONSTRUCT_SCENERY_OPCODE)
                .putA((scenery.type shl 2) or (scenery.rotation and 0x3))
                .put((location.chunkOffsetX shl 4) or (location.chunkOffsetY and 0x7))
                .putShortA(scenery.id)
            return buffer
        }
    }
}
