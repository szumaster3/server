package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * The config outgoing packet.
 *
 * @author Emperor
 */
class Config : OutgoingPacket<OutgoingContext.Config> {

    override fun send(context: OutgoingContext.Config) {
        val buffer: IoBuffer
        if (context.value < Byte.MIN_VALUE || context.value > Byte.MAX_VALUE) {
            buffer = IoBuffer(226)
            buffer.putInt(context.value)
            buffer.putShortA(context.id)
        } else {
            buffer = IoBuffer(60)
            buffer.putShortA(context.id)
            buffer.putC(context.value)
        }
        buffer.cypherOpcode(context.player.session.isaacPair.output)
        if (!context.player.isArtificial) {
            context.player.details.session.write(buffer)
        }
    }
}