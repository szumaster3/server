package content.region.misthalin.handlers.npc.varrock

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class FaridMorrisaneNPC : NPCBehavior(NPCs.FARID_MORRISANE_ORES_6523) {
    private val forceChat =
        arrayOf(
            "I can make so much money here!",
            "My father shall be so pleased.",
            "Woo hoo! What a sale!",
            "What shall I trade next....",
            "Hmm. If I divide 20 and take off 50%...",
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
