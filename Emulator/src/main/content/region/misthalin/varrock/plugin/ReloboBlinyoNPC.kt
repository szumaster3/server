package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class ReloboBlinyoNPC : NPCBehavior(NPCs.RELOBO_BLINYO_LOGS_6526) {
    private val forceChat =
        arrayOf(
            "Such a joy it is to be in this building.",
            "Hmmm. These numbers don't quite add up.",
            "My people shall be so proud!",
            "Yes! Yet another great deal.",
            "Just one more offer! One more!",
        )

    override fun onCreation(self: NPC) {
        self.isWalks = false
        self.isNeverWalks = true
    }

    private var nextChat = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextChat || !self.isPlayerNearby(12)) {
            return true
        }

        if (RandomFunction.roll(8)) {
            sendChat(self, forceChat.random())
            nextChat = now + 5000L
        }

        return true
    }
}
