package core.game.system.task;

import core.game.node.entity.Entity;
import core.game.world.map.Location;

/**
 * The interface Movement hook.
 */
public interface MovementHook {

    /**
     * Handle boolean.
     *
     * @param e the e
     * @param l the l
     * @return the boolean
     */
    boolean handle(Entity e, Location l);

}