package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The run energy outgoing packet.
 *
 * @author Emperor
 */
class RunEnergy : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val buffer = IoBuffer(234)
        buffer.put(context.player.settings.runEnergy.toInt().toByte().toInt())
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        context.player.details.session.write(buffer)
    }
}