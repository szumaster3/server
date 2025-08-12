package core.net.packet.out

import core.net.packet.IoBuffer
import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket
import shared.consts.Network

/**
 * Handles the windows pane outgoing packet.
 *
 * @author Emperor
 */
class WindowsPane : OutgoingPacket<OutgoingContext.WindowsPane> {
    override fun send(context: OutgoingContext.WindowsPane) {
        val buffer = IoBuffer(Network.WINDOW_PANE)
        buffer.cypherOpcode(context.player.session.getIsaacPair()!!.output)
        buffer.putLEShortA(context.windowId)
        buffer.putS(context.type)
        buffer.putLEShortA(context.player.interfaceManager.getPacketCount(1))
        context.player.details.session.write(buffer)
    }
}