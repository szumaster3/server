package content.region.karamja.handlers.npc

import core.api.event.applyPoison
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

val TRIBESMAN_NPC = intArrayOf(NPCs.TRIBESMAN_191, NPCs.TRIBESMAN_2496, NPCs.TRIBESMAN_2497)

class TribesmanNPC : NPCBehavior(*TRIBESMAN_NPC) {
    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        state.estimatedHit = RandomFunction.getRandom(6)

        if (RandomFunction.roll(10)) {
            applyPoison(victim, self, 30)
        }
    }
}
