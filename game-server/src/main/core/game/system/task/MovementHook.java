package core.game.system.task;

import core.game.node.entity.Entity;
import core.game.world.map.Location;

/**
 * Hook for custom movement handling logic.
 */
public interface MovementHook {

    /**
     * Invoked when an entity moves to a specific location.
     *
     * @param e The moving entity.
     * @param l The target location.
     * @return {@code true} to allow movement, {@code false} to block it.
     */
    boolean handle(Entity e, Location l);

}