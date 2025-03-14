package core.game.world.map.zone;

import core.game.node.entity.Entity;

/**
 * The interface Zone.
 */
public interface Zone {

    /**
     * Enter boolean.
     *
     * @param e the e
     * @return the boolean
     */
    boolean enter(Entity e);

    /**
     * Leave boolean.
     *
     * @param e      the e
     * @param logout the logout
     * @return the boolean
     */
    boolean leave(Entity e, boolean logout);
}
