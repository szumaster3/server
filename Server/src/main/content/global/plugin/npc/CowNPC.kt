package content.global.plugin.npc

import core.api.sendChat
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
    private var tickDelay = 0
    private val TICK_INTERVAL = 50

    override fun tick(self: NPC): Boolean {
        tickDelay++
        if (tickDelay < TICK_INTERVAL) return super.tick(self)
        tickDelay = 0

        if (RandomFunction.random(1000) == 42) {
            sendChat(self, "Moo")
        }

        return super.tick(self)
    }
}
