package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.GrandExchangeContext;

/**
 * The type Grand exchange packet.
 */
public class GrandExchangePacket implements OutgoingPacket<GrandExchangeContext> {

    private final int REMOVED = 6;
    private final int ABORTED = 2;

    @Override
    public void send(GrandExchangeContext context) {
        final IoBuffer buffer = new IoBuffer(116, PacketHeader.NORMAL);
        buffer.put(context.idx);
        if (context.state == REMOVED) {
            buffer.put(0).putShort(0).putInt(0).putInt(0).putInt(0).putInt(0);
        } else {
            byte state = (byte) (context.state + 1);
            if (context.isSell) {
                state += 8;
            }
            if (context.state == ABORTED) {
                state = context.isSell ? (byte) -3 : (byte) 5;
            }
            buffer.put(state).putShort(context.itemID).putInt(context.value).putInt(context.amt).
                    putInt(context.completedAmt).putInt(context.totalCoinsExchanged);
        }
        try {
            buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
            context.getPlayer().getSession().write(buffer);
        } catch (Exception e) {
        }
    }

}
