package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import shared.consts.Network

/**
 * The run energy outgoing packet.
 *
 * @author Emperor
 */
class RunEnergy : OutgoingPacket<OutgoingContext.PlayerContext> {
    override fun send(context: OutgoingContext.PlayerContext) {
        val buffer = IoBuffer(Network.UPDATE_RUN_ENERGY)
        buffer.put(context.player.settings.runEnergy.toInt().toByte().toInt())
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        context.player.details.session.write(buffer)
    }
}