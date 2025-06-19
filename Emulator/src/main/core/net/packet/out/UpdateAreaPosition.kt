package core.net.packet.out

import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import core.net.packet.context.AreaPositionContext

/**
 * Handles the update area position packet.
 * Sends position updates when the player moves between regions or chunks.
 * 
 * @author Emperor
 */
class UpdateAreaPosition : OutgoingPacket<AreaPositionContext> {

    override fun send(context: AreaPositionContext) {
        val buffer = getBuffer(context.player, context.location)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.session.write(buffer)
    }

    companion object {
        private const val CHUNK_UPDATE_OPCODE = 230
        private const val AREA_POSITION_OPCODE = 26

        /**
         * Gets the region chunk update buffer.
         *
         * @param player The player.
         * @param base The base location of the chunk.
         * @return The buffer.
         */
        @JvmStatic
        fun getChunkUpdateBuffer(player: Player, base: Location): IoBuffer {
            val lastSceneGraph = player.playerFlags.lastSceneGraph
            val x = base.getSceneX(lastSceneGraph)
            val y = base.getSceneY(lastSceneGraph)
            return IoBuffer(CHUNK_UPDATE_OPCODE, PacketHeader.SHORT)
                .putA(y)
                .putS(x)
        }

        /**
         * Gets the area position update buffer.
         *
         * @param player The player.
         * @param base The base location of the chunk.
         * @return The buffer.
         */
        @JvmStatic
        fun getBuffer(player: Player, base: Location): IoBuffer {
            val lastSceneGraph = player.playerFlags.lastSceneGraph
            val x = base.getSceneX(lastSceneGraph)
            val y = base.getSceneY(lastSceneGraph)
            return IoBuffer(AREA_POSITION_OPCODE)
                .putC(x)
                .put(y)
        }
    }
}
