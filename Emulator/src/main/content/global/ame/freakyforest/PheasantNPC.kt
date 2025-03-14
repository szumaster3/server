package content.global.ame.freakyforest

import content.data.GameAttributes
import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

class PheasantNPC : NPCBehavior(NPCs.PHEASANT_2459, NPCs.PHEASANT_2460, NPCs.PHEASANT_2461, NPCs.PHEASANT_2462) {
    override fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
        val assignedPheasant = getAttribute(killer, GameAttributes.RE_FREAK_TASK, -1)
        if (assignedPheasant == -1) return
        drops.removeLast()
        if (assignedPheasant == self.id) {
            drops.add(Item(Items.RAW_PHEASANT_6178))
        } else {
            drops.add(Item(Items.RAW_PHEASANT_6179))
        }
        setAttribute(killer, GameAttributes.RE_FREAK_KILLS, true)
    }

    override fun beforeDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        state.estimatedHit = 5
        state.secondaryHit = 0
    }

    override fun getXpMultiplier(
        self: NPC,
        attacker: Entity,
    ): Double {
        return 0.0
    }
}
