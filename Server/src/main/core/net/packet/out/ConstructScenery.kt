package core.net.packet.out

import core.game.node.scenery.Scenery
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.BuildSceneryContext

/**
 * The outgoing packet for constructing scenery in the player's scene.
 * @author Emperor
 */
class ConstructScenery : OutgoingPacket<BuildSceneryContext> {
    override fun send(context: BuildSceneryContext) {
        val player = context.player
        val o = context.scenery
        val buffer = write(UpdateAreaPosition.getBuffer(player, o.location.chunkBase), o)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        player.session.write(buffer)
    }

    companion object {
        @JvmStatic
        fun write(buffer: IoBuffer, scenery: Scenery): IoBuffer {
            val l = scenery.location
            buffer.put(179).putA((scenery.type shl 2) or (scenery.rotation and 0x3))
                .put((l.chunkOffsetX shl 4) or (l.chunkOffsetY and 0x7)).putShortA(scenery.id)
            return buffer
        }
    }
}
