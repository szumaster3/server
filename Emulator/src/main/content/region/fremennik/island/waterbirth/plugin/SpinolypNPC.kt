package content.region.fremennik.island.waterbirth.plugin

import core.api.event.applyPoison
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Spinolyp NPC behavior and combat.
 */
class SpinolypNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    init {
        isAggressive = true
        isNeverWalks = true
    }

    override fun init() {
        super.init()
        locks.lockMovement(Int.MAX_VALUE)
        getAggressiveHandler().isAllowTolerance = false
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = SpinolypNPC(id, location)

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = SWING_HANDLER

    override fun sendImpact(state: BattleState) {
        if (state.estimatedHit == 0 && RandomFunction.random(15) < 2) {
            state.estimatedHit = RandomFunction.random(1, 11)
        }
        if (state.estimatedHit > 11) {
            state.estimatedHit = RandomFunction.random(3, 9)
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPINOLYP_2894, NPCs.SPINOLYP_2896)


    class SpinolpySwingHandler : MultiSwingHandler(SwitchAttack(CombatStyle.MAGIC.swingHandler, null), SwitchAttack(CombatStyle.RANGE.swingHandler, null)) {
        private var currentType: CombatStyle? = null

        override fun canSwing(entity: Entity, victim: Entity): InteractionType? {
            var type = super.canSwing(entity, victim)
            if (type == InteractionType.MOVE_INTERACT) {
                type = InteractionType.NO_INTERACT
            }
            return type
        }

        override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
            val swing = super.swing(entity, victim, state)
            currentType = state?.style
            if (currentType == CombatStyle.MAGIC) {
                entity?.let { setSpell(it) }
            }
            return swing
        }

        override fun getCombatDistance(e: Entity, v: Entity, rawDistance: Int): Int = 12

        override fun calculateDefence(victim: Entity?, attacker: Entity?): Int {
            return when (currentType) {
                CombatStyle.MAGIC -> CombatStyle.MAGIC.swingHandler.calculateDefence(victim, attacker)
                CombatStyle.RANGE -> CombatStyle.RANGE.swingHandler.calculateDefence(victim, attacker)
                else -> CombatStyle.RANGE.swingHandler.calculateDefence(victim, attacker)
            }
        }

        override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
            super.visualize(entity, victim, state)
            if (state?.style == CombatStyle.MAGIC) {
                entity?.let { setSpell(it) }
                val spell = SpellBook.MODERN.getSpell(14) as CombatSpell?
                state.spell = spell
            }
            state?.style?.swingHandler?.visualize(entity, victim, state)
            entity.animator.forceAnimation(entity.properties.attackAnimation)
        }

        override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
            super.impact(entity, victim, state)
            if (currentType == CombatStyle.MAGIC && (state?.estimatedHit ?: 0) > 0) {
                victim?.getSkills()?.decrementPrayerPoints(1.0)
            } else {
                if (RandomFunction.random(20) == 5) {
                    if (victim != null && entity != null) {
                        applyPoison(victim, entity, 30)
                    }
                }
            }
        }
    }

    companion object {
        private val SWING_HANDLER: CombatSwingHandler = SpinolpySwingHandler()
        private fun setSpell(e: Entity) {
            val spell = SpellBook.MODERN.getSpell(14) as CombatSpell?
            e.properties.spell = spell
            e.properties.autocastSpell = spell
        }
    }
}
