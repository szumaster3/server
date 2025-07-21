package content.region.other.dorgeshuun.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the TindarNPC.
 */
class TindarNPC : NPCBehavior(NPCs.TINDAR_5795) {
    private var ticks = 0
    private val forceChat =
        arrayOf(
            "Crispy!",
            "Creeeespy frogs' legs!",
            "Get your crispy frogs' legs!",
            "Frogs' legs!",
            "Crispy frogs' legs!",
        )

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 20) return true

        ticks = 0
        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return super.tick(self)
    }
}
