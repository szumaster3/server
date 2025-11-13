package content.global.skill.slayer.waterbirth_dungeon

import core.api.applyPoison
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager
import core.game.world.map.Location
import core.tools.RandomFunction
import shared.consts.NPCs

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

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC =
        SpinolypNPC(id, location)

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler = SWING_HANDLER

    override fun sendImpact(state: BattleState) {
        state.estimatedHit = when {
            state.estimatedHit == 0 && RandomFunction.random(15) < 2 -> RandomFunction.random(1, 11)
            state.estimatedHit > 11 -> RandomFunction.random(3, 9)
            else -> state.estimatedHit
        }
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPINOLYP_2894, NPCs.SPINOLYP_2896)

    companion object {
        private val SPELL: CombatSpell? = SpellBookManager.SpellBook.MODERN.getSpell(14) as? CombatSpell
        private val SWING_HANDLER = object : MultiSwingHandler(
            SwitchAttack(CombatStyle.MAGIC.swingHandler, null),
            SwitchAttack(CombatStyle.RANGE.swingHandler, null)
        ) {
            private var currentType: CombatStyle? = null

            override fun canSwing(entity: Entity, victim: Entity): InteractionType {
                val type = super.canSwing(entity, victim)
                return if (type == InteractionType.MOVE_INTERACT) InteractionType.NO_INTERACT else type ?: InteractionType.NO_INTERACT
            }

            override fun swing(entity: Entity?, victim: Entity?, state: BattleState?): Int {
                val swing = super.swing(entity, victim, state)
                currentType = state?.style
                if (currentType == CombatStyle.MAGIC && entity != null) {
                    entity.properties.spell = SPELL
                    entity.properties.autocastSpell = SPELL
                }
                return swing
            }

            override fun getCombatDistance(e: Entity, v: Entity, rawDistance: Int): Int = 12

            override fun calculateDefence(victim: Entity?, attacker: Entity?): Int =
                when (currentType) {
                    CombatStyle.MAGIC -> CombatStyle.MAGIC.swingHandler.calculateDefence(victim, attacker)
                    else -> CombatStyle.RANGE.swingHandler.calculateDefence(victim, attacker)
                }

            override fun visualize(entity: Entity, victim: Entity?, state: BattleState?) {
                if (state?.style == CombatStyle.MAGIC) {
                    entity.properties.spell = SPELL
                    entity.properties.autocastSpell = SPELL
                    state.spell = SPELL
                }
                state?.style?.swingHandler?.visualize(entity, victim, state)
                entity.animator.forceAnimation(entity.properties.attackAnimation)
            }

            override fun impact(entity: Entity?, victim: Entity?, state: BattleState?) {
                if (state?.style == CombatStyle.MAGIC && (state.estimatedHit ?: 0) > 0) {
                    victim?.getSkills()?.decrementPrayerPoints(1.0)
                } else if (RandomFunction.random(20) == 5 && entity != null && victim != null) {
                    applyPoison(victim, entity, 30)
                }
            }
        }
    }
}
