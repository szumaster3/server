package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.InteractionOptionContext;

/**
 * The type Interaction option.
 */
public final class InteractionOption implements OutgoingPacket<InteractionOptionContext> {

    @Override
    public void send(InteractionOptionContext context) {
        IoBuffer buffer = new IoBuffer(44, PacketHeader.BYTE);
        buffer.putLEShortA(-1);
        if (context.isRemove()) {
            buffer.put(0);
        } else {
            buffer.put(context.getIndex() == 0 ? 1 : 0);
        }
        buffer.put(context.getIndex() + 1);
        buffer.putString(context.getName());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
        context.getPlayer().getSession().write(buffer);
    }

}