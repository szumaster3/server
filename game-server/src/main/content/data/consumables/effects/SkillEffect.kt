package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

/**
 * Modifies a specific skill level by a calculated amount based on base and bonus.
 *
 * @param skillSlot The skill to modify.
 * @param base The base amount to add or subtract.
 * @param bonus The multiplier applied to the static skill level.
 */
class SkillEffect(
    var skillSlot: Int,
    var base: Double,
    var bonus: Double,
) : ConsumableEffect() {

    /**
     * Adjusts the player's skill level by base + bonus * static level.
     */
    override fun activate(player: Player) {
        val skills = player.getSkills()
        val slevel = skills.getStaticLevel(skillSlot)
        val delta = (base + (bonus * slevel)).toInt()
        skills.updateLevel(skillSlot, delta, if (delta >= 0) slevel + delta else 0)
    }
}
