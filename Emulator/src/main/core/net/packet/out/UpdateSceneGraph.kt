package core.net.packet.out

import core.game.system.config.XteaParser.Companion.getRegionXTEA
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import core.net.packet.context.SceneGraphContext

/**
 * Sends an update scene graph packet, including region XTEA keys and player position,
 * to synchronize the client's view with the server.
 *
 * @author Emperor
 */
class UpdateSceneGraph : OutgoingPacket<SceneGraphContext> {
    override fun send(context: SceneGraphContext) {
        val buffer = IoBuffer(162, PacketHeader.SHORT)
        val player = context.player
        val location = player.location

        buffer.cypherOpcode(player.session.isaacPair.output)
        player.playerFlags.lastSceneGraph = location

        buffer.putShortA(location.sceneX)

        val regionX = location.regionX
        val regionY = location.regionY

        for (x in (regionX - RADIUS) / REGION_SIZE..(regionX + RADIUS) / REGION_SIZE) {
            for (y in (regionY - RADIUS) / REGION_SIZE..(regionY + RADIUS) / REGION_SIZE) {
                val keys = getRegionXTEA(x shl 8 or y)
                repeat(PLANE_COUNT) { plane ->
                    val key = keys?.getOrNull(plane) ?: 0
                    buffer.putIntB(key)
                }
            }
        }

        buffer.putS(location.z)
        buffer.putShort(location.regionX)
        buffer.putShortA(location.regionY)
        buffer.putShortA(location.sceneY)

        player.details.session.write(buffer)
    }

    private companion object {
        private const val RADIUS = 6
        private const val REGION_SIZE = 8
        private const val PLANE_COUNT = 4
    }
}
