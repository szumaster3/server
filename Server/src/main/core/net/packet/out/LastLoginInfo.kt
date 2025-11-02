package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingPacket
import core.net.packet.context.PlayerContext
import java.net.Inet4Address

class LastLoginInfo : OutgoingPacket<PlayerContext> {
    override fun send(context: PlayerContext) {
        val buffer = IoBuffer(164)
        buffer.imp4(Inet4Address.getByName(context.player.details.ipAddress).hashCode())
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.session.write(buffer)
    }

    // override fun send(context: PlayerContext) {
    //     val player = context.player
    //     val buffer = IoBuffer(8)
    //     val lastLoginTime = player.details.lastLogin ?: Instant.now().epochSecond
    //     buffer.putLong(lastLoginTime)
    //     buffer.cypherOpcode(player.session.isaacPair.output)
    //     player.session.write(buffer)
    // }
}