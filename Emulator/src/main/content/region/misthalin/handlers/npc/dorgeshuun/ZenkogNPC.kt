package content.region.misthalin.handlers.npc.dorgeshuun

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class ZenkogNPC : NPCBehavior(NPCs.ZENKOG_5797) {
    private val forceChat =
        arrayOf(
            "Fingers!",
            "Wall beast fingers!",
            "Fresh wall beast fingers!",
            "Lovely wall beast fingers!",
            "Tasty wall beast fingers!",
        )

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(35) == 5) {
            sendChat(self, forceChat.random())
        }
        return super.tick(self)
    }
}
