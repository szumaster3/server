package core.net.packet.out

import core.game.node.scenery.Scenery
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.BuildSceneryContext

/**
 * The outgoing packet for clearing scenery.
 * @author Emperor
 */
class ClearScenery : OutgoingPacket<BuildSceneryContext> {
    override fun send(context: BuildSceneryContext) {
        val player = context.player
        val scenery = context.scenery
        val buffer = write(UpdateAreaPosition.getBuffer(player, scenery.location.chunkBase), scenery)
        buffer.cypherOpcode(player.session.isaacPair.output)
        player.session.write(buffer)
    }

    companion object {
        @JvmStatic
        fun write(buffer: IoBuffer, scenery: Scenery): IoBuffer {
            val location = scenery.location
            return buffer.put(195) // Opcode
                .putC((scenery.type shl 2) + (scenery.rotation and 3))
                .put((location.chunkOffsetX shl 4) or (location.chunkOffsetY and 0x7))
        }
    }
}
