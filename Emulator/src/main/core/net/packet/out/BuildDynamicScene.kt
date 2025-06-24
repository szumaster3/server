package core.net.packet.out

import core.game.system.config.XteaParser.Companion.getRegionXTEA
import core.game.world.map.RegionChunk
import core.game.world.map.RegionManager.forId
import core.game.world.map.build.DynamicRegion
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader

/**
 * Represents the build dynamic scene.
 *
 * @author Emperor
 */
class BuildDynamicScene : OutgoingPacket<OutgoingContext.DynamicScene> {
    override fun send(context: OutgoingContext.DynamicScene) {
        val buffer = IoBuffer(214, PacketHeader.SHORT)
        val regionIds: MutableList<Int> = ArrayList(20)
        val player = context.player
        buffer.putLEShortA(player.location.sceneX)
        buffer.putLEShortA(player.location.regionX)
        buffer.putS(player.location.z)
        buffer.putLEShortA(player.location.sceneY)
        buffer.setBitAccess()
        var r = player.viewport.region
        val chunks = Array(4) { Array(13) { arrayOfNulls<RegionChunk>(13) } }
        val baseX = player.location.regionX - 6
        val baseY = player.location.regionY - 6
        for (z in 0..3) {
            for (x in baseX..player.location.regionX + 6) {
                for (y in baseY..player.location.regionY + 6) {
                    r = forId((x shr 3) shl 8 or (y shr 3))
                    if (r is DynamicRegion) {
                        val dr = r
                        chunks[z][x - baseX][y - baseY] = dr.chunks[z][x - (dr.x shl 3)][y - (dr.y shl 3)]
                    }
                }
            }
        }
        for (plane in 0..3) {
            for (offsetX in 0..12) {
                for (offsetY in 0..12) {
                    val c = chunks[plane][offsetX][offsetY]
                    if (c == null || c.base.x < 0 || c.base.y < 0) {
                        buffer.putBits(1, 0)
                        continue
                    }
                    val realRegionX = c.base.regionX
                    val realRegionY = c.base.regionY
                    val realPlane = c.base.z
                    val rotation = c.rotation
                    val id = (realRegionX shr 3) shl 8 or (realRegionY shr 3)
                    if (!regionIds.contains(id)) {
                        regionIds.add(id)
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
        for (id in regionIds) {
            val keys = getRegionXTEA(id)
            buffer.putIntB(keys[0]).putIntB(keys[1]).putIntB(keys[2]).putIntB(keys[3])
        }
        buffer.putShort(player.location.regionY)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.session.write(buffer)
        player.playerFlags.lastSceneGraph = player.location
    }
}