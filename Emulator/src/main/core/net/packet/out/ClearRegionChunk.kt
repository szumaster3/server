package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.ClearChunkContext

/**
 * Sends a packet to clear a region chunk for the player.
 * Ensures the chunk coordinates are within the valid scene range before sending.
 *
 * @author Emperor
 */
class ClearRegionChunk : OutgoingPacket<ClearChunkContext> {

    override fun send(context: ClearChunkContext) {
        val lastSceneGraph = context.player.playerFlags.lastSceneGraph
        val x = context.chunk.currentBase.getSceneX(lastSceneGraph)
        val y = context.chunk.currentBase.getSceneY(lastSceneGraph)
        if (x in 0 until 96 && y in 0 until 96) {
            val buffer = IoBuffer(BUFFER_SIZE).put(x).putC(y)
            buffer.cypherOpcode(context.player.session.isaacPair.output)
            context.player.session.write(buffer)
        }
    }

    private companion object {
        private const val BUFFER_SIZE = 112
    }
}
