package core.api.combat

import core.api.getWorldTicks
import core.game.interaction.Clocks
import core.game.node.entity.Entity

/**
 * Delays the next attack of an entity by a specified number of ticks.
 *
 * @param entity The entity whose attack is being delayed.
 * @param ticks The number of game ticks to delay the next attack.
 */
fun delayAttack(
    entity: Entity,
    ticks: Int,
) {
    entity.properties.combatPulse.delayNextAttack(3)
    entity.clocks[Clocks.NEXT_ATTACK] = getWorldTicks() + ticks
}

private class CombatAPI
