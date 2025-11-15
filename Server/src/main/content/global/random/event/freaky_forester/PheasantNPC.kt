package content.global.random.event.freaky_forester

import content.data.GameAttributes
import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents Pheasant NPC for Freaky Forester random event.
 * @author Zerken, szu
 */
class PheasantNPC : NPCBehavior(NPCs.PHEASANT_2459, NPCs.PHEASANT_2460, NPCs.PHEASANT_2461, NPCs.PHEASANT_2462) {

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        val assignedPheasant = getAttribute(killer, GameAttributes.RE_FREAK_TASK, -1)
        if (assignedPheasant == -1) return

        drops.removeLast()
        val itemToDrop = if (assignedPheasant == self.id) Items.RAW_PHEASANT_6178 else Items.RAW_PHEASANT_6179
        drops.add(Item(itemToDrop))

        setAttribute(killer, GameAttributes.RE_FREAK_KILLS, true)
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState) {
        state.estimatedHit = 5
        state.secondaryHit = 0
    }

    override fun getXpMultiplier(self: NPC, attacker: Entity): Double = 0.0
}
