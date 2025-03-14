package content.global.handlers.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class DuckNPC : NPCBehavior(NPCs.DUCK_46, NPCs.DUCK_2693, NPCs.DUCK_6113) {
    private val chatFrequency = 1
    private val forceChat = listOf("Eep!", "Quack!")

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(100) < chatFrequency) {
            val randomChat = forceChat[RandomFunction.random(forceChat.size)]
            sendChat(self, randomChat)
        }
        return true
    }
}
