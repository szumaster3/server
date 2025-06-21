package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import core.net.packet.PacketHeader

/**
 * The game message outgoing packet.
 *
 * @author Emperor
 */
class GameMessage : OutgoingPacket<OutgoingContext.GameMessage> {
    override fun send(context: OutgoingContext.GameMessage) {
        val buffer = IoBuffer(70, PacketHeader.BYTE)
        buffer.putString(context.message)
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.session.write(buffer)
    }
}