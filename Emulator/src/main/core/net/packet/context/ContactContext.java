package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Contact context.
 */
public final class ContactContext implements Context {

    /**
     * The constant UPDATE_STATE_TYPE.
     */
    public static final int UPDATE_STATE_TYPE = 0;

    /**
     * The constant UPDATE_FRIEND_TYPE.
     */
    public static final int UPDATE_FRIEND_TYPE = 1;

    /**
     * The constant IGNORE_LIST_TYPE.
     */
    public static final int IGNORE_LIST_TYPE = 2;

    private final Player player;

    private int type;

    private String name;

    private int worldId;

    /**
     * Instantiates a new Contact context.
     *
     * @param player the player
     * @param type   the type
     */
    public ContactContext(Player player, int type) {
        this.player = player;
        this.type = type;
    }

    /**
     * Instantiates a new Contact context.
     *
     * @param player  the player
     * @param name    the name
     * @param worldId the world id
     */
    public ContactContext(Player player, String name, int worldId) {
        this.player = player;
        this.name = name;
        this.worldId = worldId;
        this.type = UPDATE_FRIEND_TYPE;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Is online boolean.
     *
     * @return the boolean
     */
    public boolean isOnline() {
        return worldId > 0;
    }

    /**
     * Gets world id.
     *
     * @return the world id
     */
    public int getWorldId() {
        return worldId;
    }

    /**
     * Sets world id.
     *
     * @param worldId the world id
     */
    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

}