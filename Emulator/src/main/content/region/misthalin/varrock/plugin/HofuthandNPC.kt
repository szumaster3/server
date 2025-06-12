package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class HofuthandNPC : NPCBehavior(NPCs.HOFUTHAND_ARMOUR_AND_WEAPONS_6527) {
    private val forceChat =
        arrayOf(
            "I've hit the jackpot!",
            "Wow. That's cheap.",
            "This is trading, the likes of which I've never seen",
            "Oh, that didn't sell so well.",
            "Jackpot! I'm in the money now!",
            "Hmmm. If I spend twenty thousand on that, then...",
            "Hahaha! Trading the likes of which I have never seen.",
        )

    override fun onCreation(self: NPC) {
        self.isWalks = false
        self.isNeverWalks = true
    }

    private var nextChat = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextChat || !self.isPlayerNearby(15)) {
            return true
        }

        if (RandomFunction.roll(8)) {
            sendChat(self, forceChat.random())
            nextChat = now + 5000L
        }

        return true
    }
}
