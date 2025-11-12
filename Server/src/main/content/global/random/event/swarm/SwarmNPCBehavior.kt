package content.global.random.event.swarm

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles the swarm npc behavior.
 * @author Zerken
 */
class SwarmNPCBehavior : NPCBehavior(NPCs.SWARM_411) {
    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        state.estimatedHit = RandomFunction.getRandom(1)
    }
}
