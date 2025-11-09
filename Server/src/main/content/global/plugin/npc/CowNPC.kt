package content.global.plugin.npc

import core.api.sendChat
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles the CowNPC.
 */
class CowNPC : NPCBehavior(
    NPCs.COW_81,
    NPCs.COW_397,
    NPCs.COW_955,
    NPCs.COW_1767,
    NPCs.COW_3309
) {
    private var tickDelay = RandomFunction.random(5)
    private var nextChatTick = RandomFunction.random(20, 40)

    override fun tick(self: NPC): Boolean {
        if (self.properties.combatPulse.isAttacking || DeathTask.isDead(self)) {
            return true
        }

        tickDelay++

        if (tickDelay >= nextChatTick) {
            tickDelay = 0
            nextChatTick = RandomFunction.random(20, 40)

            if (RandomFunction.random(2) == 1) {
                sendChat(self, "Moo")
            }
        }

        return true
    }
}
