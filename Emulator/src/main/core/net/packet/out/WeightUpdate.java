package core.net.packet.out;

import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.PlayerContext;

/**
 * The type Weight update.
 */
public final class WeightUpdate implements OutgoingPacket<PlayerContext> {

    @Override
    public void send(PlayerContext context) {
        IoBuffer buffer = new IoBuffer(174);
        buffer.putShort((int) context.getPlayer().getSettings().getWeight());
        // TODO context.getPlayer().getSession().write(buffer);
    }

}