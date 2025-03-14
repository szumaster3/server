package core.net.packet.out;

import core.net.packet.OutgoingPacket;
import core.net.packet.context.WalkOptionContext;

/**
 * The type Set walk option.
 */
public final class SetWalkOption implements OutgoingPacket<WalkOptionContext> {

    @Override
    public void send(WalkOptionContext context) {
        // TODO IoBuffer buffer = new IoBuffer(10,
        // PacketHeader.BYTE).putString(context.getOption());
        // context.getPlayer().getDetails().getSession().write(buffer);
    }

}