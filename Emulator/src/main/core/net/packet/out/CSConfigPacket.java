package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.CSConfigContext;

/**
 * The type Cs config packet.
 */
public class CSConfigPacket implements OutgoingPacket<CSConfigContext> {

    @Override
    public void send(CSConfigContext context) {
        IoBuffer buffer = new IoBuffer(65);
        buffer.putLEShort(context.getPlayer().getInterfaceManager().getPacketCount(1));
        buffer.putC((byte) context.getValue());
        buffer.putLEShortA(context.getId());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
        context.getPlayer().getDetails().getSession().write(buffer);
    }

}