package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.context.BuildItemContext;

/**
 * The type Update ground item amount.
 */
public final class UpdateGroundItemAmount implements OutgoingPacket<BuildItemContext> {

    /**
     * Write io buffer.
     *
     * @param buffer    the buffer
     * @param item      the item
     * @param oldAmount the old amount
     * @return the io buffer
     */
    public static IoBuffer write(IoBuffer buffer, Item item, int oldAmount) {
        Location l = item.getLocation();
        buffer.put(14).put((l.getChunkOffsetX() << 4) | (l.getChunkOffsetY() & 0x7)).putShort(item.getId()).putShort(oldAmount).putShort(item.getAmount());
        return buffer;
    }

    @Override
    public void send(BuildItemContext context) {
        Player player = context.getPlayer();
        Item item = context.getItem();
        IoBuffer buffer = write(UpdateAreaPosition.getBuffer(player, item.getLocation().getChunkBase()), item, context.getOldAmount());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());
        player.getDetails().getSession().write(buffer);
    }
}