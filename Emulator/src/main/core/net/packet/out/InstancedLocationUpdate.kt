package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.LocationContext

/**
 * Sends an update of the player's location to their client only.
 *
 * @author Emperor
 */
class InstancedLocationUpdate : OutgoingPacket<LocationContext> {
    override fun send(context: LocationContext) {
        val buffer = IoBuffer(PACKET_SIZE)
        val location = context.location
        val player = context.player

        val flag = (location.z shl 1) or if (context.isTeleport) 0x1 else 0x0

        buffer.putS(flag)
        buffer.put(location.getSceneX(player.playerFlags.lastSceneGraph))
        buffer.putA(location.getSceneY(player.playerFlags.lastSceneGraph))

        player.session.write(buffer)
    }

    private companion object {
        private const val PACKET_SIZE = 110
    }
}
