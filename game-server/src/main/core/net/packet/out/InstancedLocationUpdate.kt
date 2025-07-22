package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Outgoing packet used for updating a player's location solely on his own client.
 *
 * @author Emperor
 */
class InstancedLocationUpdate : OutgoingPacket<OutgoingContext.LocationContext> {
    override fun send(context: OutgoingContext.LocationContext) {
        val buffer = IoBuffer(110)
        val l = context.location
        val player = context.player
        var flag = l.z.toInt() shl 1
        if (context.teleport) {
            flag = flag or 0x1
        }
        buffer.putS(flag)
        buffer.put(l.getSceneX(player.playerFlags.lastSceneGraph))
        buffer.putA(l.getSceneY(player.playerFlags.lastSceneGraph))
        // TODO player.getSession().write(buffer);
    }
}