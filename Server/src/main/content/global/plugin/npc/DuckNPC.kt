package content.global.plugin.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Handles the Duck NPC.
 */
class DuckNPC : NPCBehavior(NPCs.DUCK_46, NPCs.DUCK_2693, NPCs.DUCK_6113) {
    private val forceChat = arrayOf("Eep!", "Quack!")
    private var tickDelay = 0
    private val TICK_INTERVAL = 30

    override fun tick(self: NPC): Boolean {
        tickDelay++
        if (tickDelay < TICK_INTERVAL) return super.tick(self)
        tickDelay = 0

        if (RandomFunction.roll(2)) {
            sendChat(self, forceChat.random())
        }

        return super.tick(self)
    }
}
