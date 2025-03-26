package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.DefaultContext;

/**
 * The type Interface set angle.
 */
public final class InterfaceSetAngle implements OutgoingPacket<DefaultContext> {
    @Override
    public void send(DefaultContext context) {
        Player player = context.getPlayer();
        Object[] objects = context.getObjects();
        int pitch = (Integer) objects[0];
        int scale = (Integer) objects[1];
        int yaw = (Integer) objects[2];
        int interfaceId = (Integer) objects[3];
        int childId = (Integer) objects[4];
        IoBuffer buffer = new IoBuffer(132);
        buffer.putShort(pitch);
        buffer.putShortA(player.getInterfaceManager().getPacketCount(1));
        buffer.putLEShortA(scale);
        buffer.putLEShortA(yaw);
        buffer.putInt(interfaceId << 16 | childId);
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().output);
        context.getPlayer().getSession().write(buffer);
    }
}