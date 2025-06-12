package content.region.misthalin.varrock.plugin

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
