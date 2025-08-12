package core.net.packet.out

import core.game.system.config.XteaParser.Companion.getRegionXTEA
import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader
import shared.consts.Network

/**
 * The update scene graph outgoing packet.
 *
 * @author Emperor
 */
class UpdateSceneGraph : OutgoingPacket<OutgoingContext.SceneGraph> {
    override fun send(context: OutgoingContext.SceneGraph) {
        val buffer = IoBuffer(Network.UPDATE_SCENE_GRAPH, PacketHeader.SHORT)
        val player = context.player
        buffer.cypherOpcode(player.session.getIsaacPair()!!.output)
        player.playerFlags.lastSceneGraph = player.location
        buffer.putShortA(player.location.sceneX)
        for (regionX in (player.location.regionX - 6) / 8..((player.location.regionX + 6) / 8)) {
            for (regionY in (player.location.regionY - 6) / 8..((player.location.regionY + 6) / 8)) {
                val keys = getRegionXTEA(regionX shl 8 or regionY)
                for (i in 0..3) {
                    if (keys != null) buffer.putIntB(keys[i])
                    else buffer.putIntB(0)
                }
            }
        }

        buffer.putS(player.location.z)
        buffer.putShort(player.location.regionX)
        buffer.putShortA(player.location.regionY)
        buffer.putShortA(player.location.sceneY)
        player.details.session.write(buffer)
    }
}