package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.GameMessageContext;

/**
 * The type Game message.
 */
public class GameMessage implements OutgoingPacket<GameMessageContext> {

    @Override
    public void send(GameMessageContext context) {
        IoBuffer buffer = new IoBuffer(70, PacketHeader.BYTE);
        buffer.putString(context.getMessage());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
        context.getPlayer().getSession().write(buffer);
    }
}