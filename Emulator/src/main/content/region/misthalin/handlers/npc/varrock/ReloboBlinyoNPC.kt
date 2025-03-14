package content.region.misthalin.handlers.npc.varrock

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

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(35) == 5) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
