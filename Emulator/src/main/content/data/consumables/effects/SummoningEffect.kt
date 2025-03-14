package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

class SummoningEffect(
    var base: Double,
    var bonus: Double,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val level = player.getSkills().getStaticLevel(Skills.SUMMONING)

        val amt = base + (level * bonus)

        player.getSkills().updateLevel(Skills.SUMMONING, amt.toInt(), level)
    }
}
