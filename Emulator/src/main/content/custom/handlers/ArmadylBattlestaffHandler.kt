package content.custom.handlers

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.InteractionType
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager

class ArmadylBattlestaffHandler : CombatSwingHandler(CombatStyle.MAGIC) {

    override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
        return CombatStyle.MAGIC.swingHandler.canSwing(entity, victim)
    }

    override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
        if (entity is Player) {
            val player = entity.asPlayer()
            if (player.properties.autocastSpell == null || player.properties.autocastSpell !== SPELL) {
                player.properties.autocastSpell = SPELL
            }
        }
        return CombatStyle.MAGIC.swingHandler.swing(entity, victim, state)
    }

    override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
        CombatStyle.MAGIC.swingHandler.impact(entity, victim, state)
    }

    override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
        CombatStyle.MAGIC.swingHandler.visualize(entity, victim, state);
    }

    override fun visualizeImpact(entity: Entity?, victim: Entity?, state: BattleState?) {
        CombatStyle.MAGIC.swingHandler.visualizeImpact(entity, victim, state)
    }

    override fun calculateAccuracy(entity: Entity?): Int {
        return CombatStyle.MAGIC.swingHandler.calculateAccuracy(entity)
    }

    override fun calculateHit(entity: Entity?, victim: Entity?, modifier: Double): Int {
        return CombatStyle.MAGIC.swingHandler.calculateHit(entity, victim, modifier)
    }

    override fun addExperience(entity: Entity?, victim: Entity?, state: BattleState?) {
        if (entity != null) {
            SPELL!!.addExperience(entity, state!!.estimatedHit)
        }
    }

    override fun calculateDefence(entity: Entity?, attacker: Entity?): Int {
        return CombatStyle.MAGIC.swingHandler.calculateDefence(entity, attacker)
    }

    override fun getSetMultiplier(e: Entity?, skillId: Int): Double {
        return CombatStyle.MAGIC.swingHandler.getSetMultiplier(e, skillId)
    }

    companion object {
        private val SPELL = SpellBookManager.SpellBook.forInterface(90)!!.register(5, StormOfArmadylSpell()) as CombatSpell?

        @JvmField
        val INSTANCE: ArmadylBattlestaffHandler = ArmadylBattlestaffHandler()
    }
}
