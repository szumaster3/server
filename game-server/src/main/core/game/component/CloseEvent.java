package core.game.component;

import core.game.node.entity.player.Player;

/**
 * The interface Close event.
 */
public interface CloseEvent {

    /**
     * Close boolean.
     *
     * @param player the player
     * @param c      the c
     * @return the boolean
     */
    boolean close(Player player, Component c);

}