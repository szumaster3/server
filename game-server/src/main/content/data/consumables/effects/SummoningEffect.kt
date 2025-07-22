package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Boosts the player Summoning skill level by a calculated amount.
 *
 * @param base The base amount to add.
 * @param bonus The multiplier applied to the player's static Summoning level.
 */
class SummoningEffect(
    var base: Double,
    var bonus: Double,
) : ConsumableEffect() {

    /**
     * Increases Summoning level by base plus bonus times static level.
     */
    override fun activate(player: Player) {
        val level = player.getSkills().getStaticLevel(Skills.SUMMONING)
        val amt = base + (level * bonus)
        player.getSkills().updateLevel(Skills.SUMMONING, amt.toInt(), level)
    }
}
