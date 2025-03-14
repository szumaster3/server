package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Access mask context.
 */
public class AccessMaskContext implements Context {

    private Player player;

    private int id;

    private int childId;

    private int interfaceId;

    private int offset;

    private int length;

    /**
     * Instantiates a new Access mask context.
     *
     * @param player      the player
     * @param id          the id
     * @param childId     the child id
     * @param interfaceId the interface id
     * @param offset      the offset
     * @param length      the length
     */
    public AccessMaskContext(Player player, int id, int childId, int interfaceId, int offset, int length) {
        this.player = player;
        this.id = id;
        this.childId = childId;
        this.interfaceId = interfaceId;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Transform access mask context.
     *
     * @param player the player
     * @param id     the id
     * @return the access mask context
     */
    public AccessMaskContext transform(Player player, int id) {
        return new AccessMaskContext(player, id, childId, interfaceId, offset, length);
    }

    /**
     * Sets player.
     *
     * @param player the player
     * @return the player
     */
    public Context setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets child id.
     *
     * @return the child id
     */
    public int getChildId() {
        return childId;
    }

    /**
     * Gets interface id.
     *
     * @return the interface id
     */
    public int getInterfaceId() {
        return interfaceId;
    }

    /**
     * Gets offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets length.
     *
     * @return the length
     */
    public int getLength() {
        return length;
    }
}
