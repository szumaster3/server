package core.api.event

import core.api.getTimer
import core.api.registerTimer
import core.api.spawnTimer
import core.game.node.entity.Entity
import core.game.system.timer.RSTimer

/**
 * Retrieves an existing timer for the specified entity and identifier, or starts a new one if none exists.
 *
 * @param entity The entity for which the timer is being retrieved or created.
 * @param identifier The unique identifier for the timer.
 * @param args The additional arguments to pass when creating the timer, if needed.
 * @return The existing timer if one exists, or the newly created timer if no existing timer is found.
 */
fun getOrStartTimer(
    entity: Entity,
    identifier: String,
    vararg args: Any,
): RSTimer? {
    val existing = getTimer(entity, identifier)
    if (existing != null) return existing
    return spawnTimer(identifier, *args).also { registerTimer(entity, it) }
}

private class EventScheduler
