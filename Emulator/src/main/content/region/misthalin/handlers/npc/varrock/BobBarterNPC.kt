package content.region.misthalin.handlers.npc.varrock

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class BobBarterNPC : NPCBehavior(NPCs.BOB_BARTER_HERBS_6524) {
    private val forceChat =
        arrayOf(
            "Now, what should I buy?",
            "I'm in the money!",
            "I could have sworn that would have worked.",
            "Hope this item sells well.",
            "Please, please, please work.",
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
