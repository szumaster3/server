package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

/**
 * The type Interaction option context.
 */
public final class InteractionOptionContext implements Context {

    private final Player player;

    private final int index;
    private final String name;
    private boolean remove = false;

    /**
     * Instantiates a new Interaction option context.
     *
     * @param player the player
     * @param index  the index
     * @param name   the name
     */
    public InteractionOptionContext(Player player, int index, String name) {
        this.player = player;
        this.index = index;
        this.name = name;
        this.remove = false;
    }

    /**
     * Instantiates a new Interaction option context.
     *
     * @param player the player
     * @param index  the index
     * @param name   the name
     * @param remove the remove
     */
    public InteractionOptionContext(Player player, int index, String name, boolean remove) {
        this.player = player;
        this.index = index;
        this.name = name;
        this.remove = remove;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    /**
     * Is remove boolean.
     *
     * @return the boolean
     */
    public boolean isRemove() {
        return remove;
    }

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

}