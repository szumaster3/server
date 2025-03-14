package core.api.movement

import core.game.interaction.Clocks
import core.game.node.entity.Entity
import core.game.world.GameWorld

/**
 * Checks if the specified entity has finished its movement.
 *
 * @param entity The entity whose movement status is being checked.
 * @return True if the entity has finished moving, otherwise false.
 */
fun finishedMoving(entity: Entity): Boolean {
    return entity.clocks[Clocks.MOVEMENT] < GameWorld.ticks
}

private class MovementAPI
