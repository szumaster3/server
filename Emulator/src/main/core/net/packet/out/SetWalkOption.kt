package core.net.packet.out

import core.net.packet.OutgoingContext
import core.net.packet.OutgoingPacket

/**
 * Handles the sending of the "Set walk-to option" packet.
 * @author Emperor
 */
class SetWalkOption : OutgoingPacket<OutgoingContext.WalkOption> {
    override fun send(context: OutgoingContext.WalkOption) {
        // TODO IoBuffer buffer = new IoBuffer(10,
        // PacketHeader.BYTE).putString(context.getOption());
        // context.getPlayer().getDetails().getSession().write(buffer);
    }
}