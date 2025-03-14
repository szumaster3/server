package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

class RestoreEffect : ConsumableEffect {
    var base: Double
    var bonus: Double
    var skills: Boolean

    constructor(base: Double, bonus: Double) {
        this.base = base
        this.bonus = bonus
        this.skills = false
    }

    constructor(base: Double, bonus: Double, skills: Boolean) {
        this.base = base
        this.bonus = bonus
        this.skills = skills
    }

    val SKILLS: IntArray = intArrayOf(Skills.DEFENCE, Skills.ATTACK, Skills.STRENGTH, Skills.MAGIC, Skills.RANGE)
    val ALL_SKILLS: IntArray =
        intArrayOf(
            Skills.ATTACK,
            Skills.DEFENCE,
            Skills.STRENGTH,
            Skills.RANGE,
            Skills.PRAYER,
            Skills.MAGIC,
            Skills.COOKING,
            Skills.WOODCUTTING,
            Skills.FLETCHING,
            Skills.FISHING,
            Skills.FIREMAKING,
            Skills.CRAFTING,
            Skills.SMITHING,
            Skills.MINING,
            Skills.HERBLORE,
            Skills.AGILITY,
            Skills.THIEVING,
            Skills.SLAYER,
            Skills.FARMING,
            Skills.RUNECRAFTING,
            Skills.HUNTER,
            Skills.CONSTRUCTION,
            Skills.SUMMONING,
        )

    override fun activate(p: Player) {
        val sk = p.getSkills()
        val skills = if (this.skills) ALL_SKILLS else SKILLS
        for (skill in skills) {
            val statL = sk.getStaticLevel(skill)
            val curL = sk.getLevel(skill)
            if (curL < statL) {
                val boost = (base + (statL * bonus)).toInt()
                p.getSkills().updateLevel(skill, boost, statL)
            }
        }
    }
}
