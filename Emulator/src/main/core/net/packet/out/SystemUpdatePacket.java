package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.SystemUpdateContext;

/**
 * The type System update packet.
 */
public class SystemUpdatePacket implements OutgoingPacket<SystemUpdateContext> {

    @Override
    public void send(final SystemUpdateContext context) {
        IoBuffer buffer = new IoBuffer(85).putShort(context.getTime());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
        context.getPlayer().getDetails().getSession().write(buffer);
    }

}
