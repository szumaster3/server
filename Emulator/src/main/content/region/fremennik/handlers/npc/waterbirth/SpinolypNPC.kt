package content.region.fremennik.handlers.npc.waterbirth

import core.api.event.applyPoison
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.map.Location
import core.tools.RandomFunction

class SpinolypNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    init {
        super.setAggressive(true)
        super.setNeverWalks(true)
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return SpinolypNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return SWING_HANDLER
    }

    override fun init() {
        super.init()
        super.getLocks().lockMovement(Int.MAX_VALUE)
        setSpell()
        getAggressiveHandler().isAllowTolerance = false
    }

    fun setSpell() {
        val spell = SpellBook.MODERN.getSpell(14) as CombatSpell?
        properties.spell = spell
        properties.autocastSpell = spell
    }

    override fun sendImpact(state: BattleState) {
        if (state.estimatedHit == 0 && RandomFunction.random(15) < 2) {
            state.estimatedHit = RandomFunction.random(1, 11)
        }
        if (state.estimatedHit > 11) {
            state.estimatedHit = RandomFunction.random(3, 9)
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(2894, 2896)
    }

    class SpinolpySwingHandler :
        MultiSwingHandler(
            SwitchAttack(CombatStyle.MAGIC.swingHandler, null),
            SwitchAttack(CombatStyle.RANGE.swingHandler, null),
        ) {
        override fun canSwing(
            entity: Entity,
            victim: Entity,
        ): InteractionType? {
            var type = super.canSwing(entity, victim)
            if (type == InteractionType.MOVE_INTERACT) {
                type = InteractionType.NO_INTERACT
            }
            return type
        }

        override fun swing(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ): Int {
            val swing = super.swing(entity, victim, state)
            if (type == CombatStyle.MAGIC) {
                setSpell(entity)
            }
            return swing
        }

        override fun getCombatDistance(
            e: Entity,
            v: Entity,
            rawDistance: Int,
        ): Int {
            return 12
        }

        override fun calculateDefence(
            victim: Entity?,
            attacker: Entity?,
        ): Int {
            return CombatStyle.RANGE.swingHandler.calculateDefence(victim, attacker)
        }

        override fun visualize(
            entity: Entity,
            victim: Entity?,
            state: BattleState?,
        ) {
            super.visualize(entity, victim, state)
            if (state!!.style == CombatStyle.MAGIC) {
                setSpell(entity)
                val spell = SpellBook.MODERN.getSpell(14) as CombatSpell?
                state.spell = spell
            }
            state.style.swingHandler.visualize(entity, victim, state)
            entity.animator.forceAnimation(entity.properties.attackAnimation)
        }

        override fun impact(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ) {
            super.impact(entity, victim, state)
            if (super.type == CombatStyle.MAGIC && state!!.estimatedHit > 0) {
                victim!!.getSkills().decrementPrayerPoints(1.0)
            } else {
                if (RandomFunction.random(20) == 5) {
                    applyPoison(victim!!, entity!!, 30)
                }
            }
        }

        fun setSpell(e: Entity?) {
            val spell = SpellBook.MODERN.getSpell(14) as CombatSpell?
            e!!.properties.spell = spell
            e.properties.autocastSpell = spell
        }
    }

    companion object {
        private val SWING_HANDLER: CombatSwingHandler = SpinolpySwingHandler()
    }
}
