package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingContext;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;

/**
 * The outgoing packet used for updating a player's grand exchange data.
 *
 * @author Emperor, Vexia, Angle
 */
public class GrandExchangePacket implements OutgoingPacket<OutgoingContext.GrandExchange> {

    private final int REMOVED = 6;
    private final int ABORTED = 2;

    @Override
    public void send(OutgoingContext.GrandExchange context) {
        final IoBuffer buffer = new IoBuffer(116, PacketHeader.NORMAL);
        buffer.put(context.getIdx());
        if (context.getState() == REMOVED) {
            buffer.put(0).putShort(0).putInt(0).putInt(0).putInt(0).putInt(0);
        } else {
            byte state = (byte) (context.getState() + 1);
            if (context.isSell()) {
                state += 8;
            }
            if (context.getState() == ABORTED) {
                state = context.isSell() ? (byte) -3 : (byte) 5;
            }
            buffer.put(state).putShort(context.getItemID()).putInt(context.getValue()).putInt(context.getAmt()).
                    putInt(context.getCompletedAmt()).putInt(context.getTotalCoinsExchanged());
        }
        try {
            buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
            context.getPlayer().getSession().write(buffer);
        } catch (Exception e) {
        }
    }

}
