package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Outgoing music packet.
 *
 * @author SonicForce41
 */
class MusicPacket : OutgoingPacket<OutgoingContext.Music> {
    override fun send(context: OutgoingContext.Music) {
        var buffer: IoBuffer? = null
        if (context.secondary) {
            buffer = IoBuffer(208)
            buffer.putTri(255)
            buffer.putLEShort(context.musicId)
        } else {
            buffer = IoBuffer(4)
            buffer.putLEShortA(context.musicId)
        }
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.details.session.write(buffer)
    }
}
