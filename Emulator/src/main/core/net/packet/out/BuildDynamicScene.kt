package core.net.packet.out

import core.game.system.config.XteaParser.Companion.getRegionXTEA
import core.game.world.map.RegionChunk
import core.game.world.map.RegionManager.forId
import core.game.world.map.build.DynamicRegion
import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import core.net.packet.context.DynamicSceneContext

/**
 * Handles sending region and chunk data with rotation and plane information.
 *
 * @author Emperor
 */
class BuildDynamicScene : OutgoingPacket<DynamicSceneContext> {
    override fun send(context: DynamicSceneContext) {
        val player = context.player
        val location = player.location
        val buffer = IoBuffer(DYNAMIC_SCENE_OPCODE, PacketHeader.SHORT)
        val regionIds = mutableListOf<Int>()

        buffer.putLEShortA(location.sceneX)
        buffer.putLEShortA(location.regionX)
        buffer.putS(location.z)
        buffer.putLEShortA(location.sceneY)
        buffer.setBitAccess()

        val chunks = Array(PLANE_COUNT) { Array(CHUNK_SIZE) { arrayOfNulls<RegionChunk>(CHUNK_SIZE) } }
        val baseX = location.regionX - REGION_RADIUS
        val baseY = location.regionY - REGION_RADIUS

        for (z in 0 until PLANE_COUNT) {
            for (x in baseX..location.regionX + REGION_RADIUS) {
                for (y in baseY..location.regionY + REGION_RADIUS) {
                    val region = forId((x shr 3) shl 8 or (y shr 3))
                    if (region is DynamicRegion) {
                        val chunk = region.chunks[z][x - (region.x shl 3)][y - (region.y shl 3)]
                        chunks[z][x - baseX][y - baseY] = chunk
                    }
                }
            }
        }

        for (plane in 0 until PLANE_COUNT) {
            for (offsetX in 0 until CHUNK_SIZE) {
                for (offsetY in 0 until CHUNK_SIZE) {
                    val chunk = chunks[plane][offsetX][offsetY]
                    if (chunk == null || chunk.base.x < 0 || chunk.base.y < 0) {
                        buffer.putBits(1, 0)
                        continue
                    }

                    val realRegionX = chunk.base.regionX
                    val realRegionY = chunk.base.regionY
                    val realPlane = chunk.base.z
                    val rotation = chunk.rotation
                    val regionId = (realRegionX shr 3) shl 8 or (realRegionY shr 3)

                    if (regionId !in regionIds) {
                        regionIds.add(regionId)
                    }

                    buffer.putBits(1, 1)
                    buffer.putBits(
                        26,
                        (rotation shl 1) or (realPlane shl 24) or (realRegionX shl 14) or (realRegionY shl 3)
                    )
                }
            }
        }

        buffer.setByteAccess()
        for (regionId in regionIds) {
            val keys = getRegionXTEA(regionId)
            keys.forEach { buffer.putIntB(it) }
        }

        buffer.putShort(location.regionY)
        buffer.cypherOpcode(player.session.isaacPair.output)
        player.session.write(buffer)
        player.playerFlags.lastSceneGraph = location
    }

    private companion object {
        private const val DYNAMIC_SCENE_OPCODE = 214
        private const val PLANE_COUNT = 4
        private const val CHUNK_SIZE = 13
        private const val REGION_RADIUS = 6
    }
}
