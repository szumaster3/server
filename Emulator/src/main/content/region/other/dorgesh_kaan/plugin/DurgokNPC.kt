package content.region.other.dorgesh_kaan.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class DurgokNPC : NPCBehavior(NPCs.DURGOK_5794) {

    private val forceChat = arrayOf(
        "Burgers!",
        "Hot frogburgers!",
        "Frogburgers!",
        "Frog in a bun!",
        "Tasty frogburgers!"
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
