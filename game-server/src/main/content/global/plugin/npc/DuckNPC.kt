package content.global.plugin.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Duck NPC.
 */
class DuckNPC : NPCBehavior(NPCs.DUCK_46, NPCs.DUCK_2693, NPCs.DUCK_6113) {
    private val forceChat = listOf("Eep!", "Quack!")
    private var tickDelay = 0
    private val TICK_INTERVAL = 30

    override fun tick(self: NPC): Boolean {
        tickDelay++
        if (tickDelay < TICK_INTERVAL) return true
        tickDelay = 0

        if (RandomFunction.random(45) == 5) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}

