package core.api.event

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.system.timer.impl.Disease
import core.game.system.timer.impl.Poison
import core.game.system.timer.impl.PoisonImmunity

/**
 * Cures the poison effect from an entity if it is currently poisoned.
 *
 * @param entity The entity to cure of poison.
 */
fun curePoison(entity: Entity) {
    if (!hasTimerActive<Poison>(entity)) return
    removeTimer<Poison>(entity)
    if (entity is Player) sendMessage(entity, "Your poison has been cured.")
}

/**
 * Cures the disease effect from an entity.
 *
 * @param entity The entity to cure of disease.
 */
fun cureDisease(entity: Entity) {
    if (!hasTimerActive<Disease>(entity)) return
    removeTimer<Disease>(entity)
    if (entity is Player) sendMessage(entity, "Your disease has been cured.")
}

/**
 * Checks if an entity is currently poisoned.
 *
 * @param entity The entity to check for poison.
 * @return True if the entity is poisoned, otherwise false.
 */
fun isPoisoned(entity: Entity): Boolean {
    return getTimer<Poison>(entity) != null
}

/**
 * Checks if an entity is currently diseased.
 *
 * @param entity The entity to check for disease.
 * @return True if the entity is diseased, otherwise false.
 */
fun isDiseased(entity: Entity): Boolean {
    return getTimer<Disease>(entity) != null
}

/**
 * Checks if an entity is currently stunned.
 *
 * @param entity The entity to check for stun status.
 * @return True if the entity is stunned, otherwise false.
 */
fun isStunned(entity: Entity): Boolean {
    return entity.clocks[Clocks.STUN] >= getWorldTicks()
}

/**
 * Applies a poison effect to an entity, or refreshes the existing poison effect if already active.
 *
 * @param entity The entity to apply or refresh the poison effect on.
 * @param source The entity responsible for applying the poison (e.g., attacker or source of poison).
 * @param severity The number of remaining hits for the poison effect.
 */
fun applyPoison(
    entity: Entity,
    source: Entity,
    severity: Int,
) {
    if (hasTimerActive<PoisonImmunity>(entity)) {
        return
    }
    val existingTimer = getTimer<Poison>(entity)

    if (existingTimer != null) {
        existingTimer.severity = severity
        existingTimer.damageSource = source
    } else {
        registerTimer(entity, spawnTimer<Poison>(source, severity))
    }
}

/**
 * Applies a disease effect to an entity, or refreshes the existing disease effect if already active.
 *
 * @param entity The entity to apply or refresh the disease effect on.
 * @param source The entity responsible for applying the disease (e.g., attacker or source of disease).
 * @param hits The number of remaining hits for the disease effect.
 */
fun applyDisease(
    entity: Entity,
    source: Entity,
    hits: Int,
) {
    if (hasTimerActive<Disease>(entity)) {
        return
    }
    val existingTimer = getTimer<Disease>(entity)

    if (existingTimer != null) {
        existingTimer.hitsLeft = hits
    } else {
        registerTimer(entity, spawnTimer<Disease>(source, hits))
    }
}

/**
 * Updates the player's credit balance by adding the specified amount.
 *
 * @param player The player whose credits are being updated.
 * @param amount The amount to adjust the player's credits by. Positive values add credits, negative values subtract.
 * @return true if the update was successful, false if the resulting balance would be negative.
 */
fun updateCredits(
    player: Player,
    amount: Int,
): Boolean {
    val creds = getCredits(player) + amount

    if (creds < 0) {
        return false
    } else {
        player.details.accountInfo.credits = creds
    }

    return true
}

/**
 * Retrieves the current credit balance of the player.
 *
 * @param player The player whose credit balance is being retrieved.
 * @return The player's current credit balance.
 */
fun getCredits(player: Player): Int {
    return player.details.accountInfo.credits
}

private class EventAPI
