package content.region.other.dorgesh_kaan.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class ZenkogNPC : NPCBehavior(NPCs.ZENKOG_5797) {
    private val forceChat = arrayOf(
        "Fingers!",
        "Wall beast fingers!",
        "Fresh wall beast fingers!",
        "Lovely wall beast fingers!",
        "Tasty wall beast fingers!",
    )

    private var nextChat = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextChat || !self.isPlayerNearby(15)) return true

        if (RandomFunction.roll(8)) {
            sendChat(self, forceChat.random())
            nextChat = now + 10000L
        }

        return true
    }
}
