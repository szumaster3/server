package core.net.packet;

import core.game.node.entity.player.Player;

/**
 * The interface Incoming packet.
 */
public interface IncomingPacket {

    /**
     * Decode.
     *
     * @param player the player
     * @param opcode the opcode
     * @param buffer the buffer
     */
    public void decode(Player player, int opcode, IoBuffer buffer);

}