package content.global.skill.slayer.items

import content.global.skill.slayer.SlayerEquipmentFlags
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Handler for the Mirror Shield special effect.
 */
object MirrorShieldHandler : MeleeSwingHandler() {
    private val SKILLS = intArrayOf(Skills.ATTACK, Skills.STRENGTH, Skills.DEFENCE, Skills.RANGE)

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (victim is Player) {
            if (!hasShield(victim)) {
                state?.estimatedHit = 11
                for (skill in SKILLS) {
                    val drain = (victim.skills.getStaticLevel(skill) * 0.25).toInt()
                    victim.skills.updateLevel(skill, -drain, victim.skills.getStaticLevel(skill) - drain)
                }
            }
        }
        super.impact(entity, victim, state)
    }

    /**
     * Checks and modifies the impact state if the attacker does not have the mirror shield equipped.
     */
    fun checkImpact(state: BattleState) {
        val attacker = state.attacker
        if (attacker is Player) {
            if (!hasShield(attacker)) {
                state.estimatedHit = 0
                if (state.secondaryHit > 0) {
                    state.secondaryHit = 0
                }
            }
        }
    }

    private fun hasShield(player: Player): Boolean =
        SlayerEquipmentFlags.hasMirrorShield(player)
}
