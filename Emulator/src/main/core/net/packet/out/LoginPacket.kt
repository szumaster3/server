package core.net.packet.out

import core.game.node.entity.player.info.Rights
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import java.nio.ByteBuffer

/**
 * Handles the login outgoing packet.
 *
 * @author Emperor
 */
class LoginPacket : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val p = context.player
        val buffer = ByteBuffer.allocate(9)
        val right =
            if (context.player.details.rights === Rights.PLAYER_MODERATOR) 1 else if (context.player.details.rights === Rights.ADMINISTRATOR) 2 else 0
        buffer.put((right).toByte())
        buffer.put(0.toByte()) // Something with client scripts, maybe login //
        // screen?
        buffer.put(0.toByte()) // No idea.
        buffer.put(0.toByte()) // No idea.
        buffer.put(1.toByte()) // Boolean, possibly members.
        buffer.putShort(p.index.toShort())
        buffer.put(1.toByte()) // No idea. (something with client scripts, again)
        p.details.session.write(buffer)
    }
}