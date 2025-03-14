package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player

class SkillEffect(
    var skillSlot: Int,
    var base: Double,
    var bonus: Double,
) : ConsumableEffect() {
    override fun activate(player: Player) {
        val skills = player.getSkills()
        val slevel = skills.getStaticLevel(skillSlot)
        val delta = (base + (bonus * slevel)).toInt()
        skills.updateLevel(skillSlot, delta, if (delta >= 0) slevel + delta else 0)
    }
}
