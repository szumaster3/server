package content.global.ame.drunkdwarf

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Represents the Sergeant Damien NPC behavior inside random event.
 * @author Zerken (October 13, 2023)
 */
class DrunkenDwarfBehavior : NPCBehavior(NPCs.DRUNKEN_DWARF_956) {
    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        state.estimatedHit = RandomFunction.getRandom(3)
    }
}
