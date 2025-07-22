package content.region.fremennik.neitiznot.npc

import core.api.sendChat
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Yak NPC.
 */
class YakNPC : NPCBehavior(NPCs.YAK_5529) {
    override fun tick(self: NPC): Boolean {
        if (self.properties.combatPulse.isAttacking || DeathTask.isDead(self)) {
            return true
        }
        if (RandomFunction.random(45) == 5) {
            sendChat(self, "Moo")
        }
        return super.tick(self)
    }
}
