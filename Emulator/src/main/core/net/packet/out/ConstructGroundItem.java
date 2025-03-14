package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.BuildItemContext;

/**
 * The type Construct ground item.
 */
public final class ConstructGroundItem implements OutgoingPacket<BuildItemContext> {

    /**
     * Write io buffer.
     *
     * @param buffer the buffer
     * @param item   the item
     * @return the io buffer
     */
    public static IoBuffer write(IoBuffer buffer, Item item) {
        Location l = item.getLocation();
        buffer.put(33);
        buffer.putLEShort(item.getId()).put((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShortA(item.getAmount());
        return buffer;
    }

    @Override
    public void send(BuildItemContext context) {
        Player player = context.getPlayer();
        Item item = context.getItem();
        IoBuffer buffer = write(UpdateAreaPosition.getBuffer(player, item.getLocation().getChunkBase()), item);
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());
        player.getDetails().getSession().write(buffer);
    }
}
