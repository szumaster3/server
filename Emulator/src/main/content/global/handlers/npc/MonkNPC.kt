package content.global.handlers.npc

import core.api.animate
import core.api.stopWalk
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MeleeSwingHandler
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.NPCs

@Initializable
class MonkNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return MonkNPC(id, location)
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return CombatAction()
    }

    private class CombatAction : MeleeSwingHandler() {
        override fun swing(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ): Int {
            if (entity!!.getSkills().lifepoints != entity.getSkills().maximumLifepoints &&
                RandomFunction.randomize(10) < 2
            ) {
                stopWalk(entity)
                animate(entity, Animations.HANDS_TOGETHER_709)
                entity.getSkills().heal(2)
                entity.properties.combatPulse.setNextAttack(4)
                return -1
            }
            return super.swing(entity, victim, state)
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MONK_7727)
    }
}
