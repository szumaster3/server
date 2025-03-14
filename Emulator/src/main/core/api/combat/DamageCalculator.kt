package core.api.combat

import core.api.EquipmentSlot
import core.api.getItemFromEquipment
import core.api.hasTimerActive
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.system.timer.impl.DragonFireImmunity
import org.rs.consts.Items

/**
 * Calculates the maximum damage a player or entity will take from a dragon's fire breath,
 * considering the presence of protection methods such as dragonfire shields, potions, and prayers.
 *
 * This function applies damage reduction based on the player's or entity's protection
 * from dragonfire. The final damage is calculated by reducing the maximum possible damage
 * according to whether the entity has protection from fire breath via shield, potion, or prayer.
 * If no protection is available, the full damage is taken.
 *
 * @param entity The entity (either player or NPC) receiving the dragonfire attack.
 * @param maxDamage The maximum potential damage that the dragonfire breath can cause.
 * @param wyvern A flag indicating if the dragonfire is from a wyvern. Defaults to false (meaning it's regular dragonfire).
 * @param unprotectableDamage The portion of damage that cannot be reduced by any protection. Defaults to 0.
 * @param sendMessage A flag to send a message to the entity about the protection effect. Defaults to false.
 * @return The final calculated damage after applying the protection effects.
 *         If protection is present, it will reduce the damage accordingly.
 */
fun calculateDragonFireMaxHit(
    entity: Entity,
    maxDamage: Int,
    wyvern: Boolean = false,
    unprotectableDamage: Int = 0,
    sendMessage: Boolean = false,
): Int {
    val hasShield: Boolean
    val hasPotion: Boolean
    val hasPrayer: Boolean

    if (entity is Player) {
        // Check for player-specific protection methods.
        hasShield = hasDragonfireShieldProtection(entity, wyvern)
        hasPotion = !wyvern && hasTimerActive<DragonFireImmunity>(entity)
        hasPrayer = entity.prayer.get(PrayerType.PROTECT_FROM_MAGIC)

        if (sendMessage) {
            var message = "You are horribly burnt by the ${if (wyvern) "icy" else "fiery"} breath."
            if (hasShield && hasPotion) {
                message =
                    "Your potion and shield fully absorb the ${if (wyvern) "icy" else "fiery"} breath."
            } else if (hasShield) {
                message = "Your shield absorbs most of the ${if (wyvern) "icy" else "fiery"} breath."
            } else if (hasPotion) {
                message = "Your potion absorbs some of the fiery breath."
            } else if (hasPrayer) {
                message = "Your prayer absorbs some of the ${if (wyvern) "icy" else "fiery"} breath."
            }
            sendMessage(entity, message)
        }
    } else {
        // Check for NPC-specific protection methods.
        val dragonFireTokens = entity.getDragonfireProtection(!wyvern)
        hasShield = dragonFireTokens and 0x2 != 0
        hasPotion = dragonFireTokens and 0x4 != 0
        hasPrayer = dragonFireTokens and 0x8 != 0
    }

    var effectiveDamage = maxDamage.toDouble()
    if (hasPrayer && !hasShield && !hasPotion) {
        effectiveDamage -= 0.6 * maxDamage
    } else {
        if (hasShield) effectiveDamage -= 0.9 * maxDamage
        if (hasPotion) effectiveDamage -= 0.1 * maxDamage
    }

    return Math.max(unprotectableDamage, effectiveDamage.toInt())
}

/**
 * Checks if the player has protection against dragonfire or wyvern attacks based on their equipped shield.
 *
 * @param player The player whose equipment is being checked for protection.
 * @param wyvern If true, checks for protection against wyvern attacks; otherwise, checks for dragonfire protection.
 * @return True if the player has the appropriate shield for protection, otherwise false.
 */
fun hasDragonfireShieldProtection(
    player: Player,
    wyvern: Boolean = false,
): Boolean {
    val shield = getItemFromEquipment(player, EquipmentSlot.SHIELD) ?: return false
    return when (shield.id) {
        Items.ELEMENTAL_SHIELD_2890, Items.MIND_SHIELD_9731 -> wyvern
        Items.ANTI_DRAGON_SHIELD_1540 -> !wyvern
        Items.DRAGONFIRE_SHIELD_11283, Items.DRAGONFIRE_SHIELD_11284 -> true
        else -> false
    }
}

private class DamageCalculator
