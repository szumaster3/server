package core.api.movement

import core.ServerConstants
import core.api.utils.Vector
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import kotlin.math.min

/**
 * Truncates the movement path of an entity towards a destination location if the distance is too far.
 *
 * @param mover The entity (either player or NPC) that is moving.
 * @param destination The target destination location to move towards.
 * @return A pair where the first value indicates whether the path was truncated,
 *         and the second value is the new truncated destination location.
 */
fun truncateLoc(
    mover: Entity,
    destination: Location,
): Pair<Boolean, Location> {
    val vector = Vector.betweenLocs(mover.location, destination)
    val normVec = vector.normalized()
    val mag = vector.magnitude()

    var multiplier = if (mover is NPC) 14.0 else ServerConstants.MAX_PATHFIND_DISTANCE.toDouble()
    var clampedMultiplier = min(multiplier, mag)

    var truncated = multiplier == clampedMultiplier

    return Pair(truncated, mover.location.transform(normVec * clampedMultiplier))
}

private class PathfindingAPI
