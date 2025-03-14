package core.net.packet.out;

import core.game.node.entity.player.Player;
import core.game.world.map.Location;
import core.net.packet.IoBuffer;
import core.net.packet.OutgoingPacket;
import core.net.packet.PacketHeader;
import core.net.packet.context.AreaPositionContext;

/**
 * The type Update area position.
 */
public final class UpdateAreaPosition implements OutgoingPacket<AreaPositionContext> {

    /**
     * Gets chunk update buffer.
     *
     * @param player the player
     * @param base   the base
     * @return the chunk update buffer
     */
    public static IoBuffer getChunkUpdateBuffer(Player player, Location base) {
        int x = base.getSceneX(player.getPlayerFlags().getLastSceneGraph());
        int y = base.getSceneY(player.getPlayerFlags().getLastSceneGraph());
        return new IoBuffer(230, PacketHeader.SHORT).putA(y).putS(x);
    }

    /**
     * Gets buffer.
     *
     * @param player the player
     * @param base   the base
     * @return the buffer
     */
    public static IoBuffer getBuffer(Player player, Location base) {
        int x = base.getSceneX(player.getPlayerFlags().getLastSceneGraph());
        int y = base.getSceneY(player.getPlayerFlags().getLastSceneGraph());
        return new IoBuffer(26).putC(x).put(y);
    }

    @Override
    public void send(AreaPositionContext context) {
        IoBuffer buffer = getBuffer(context.getPlayer(), context.getLocation());
        buffer.cypherOpcode(context.getPlayer().getSession().getIsaacPair().getOutput());
        context.getPlayer().getSession().write(buffer);
    }

}