package content.region.misthalin.handlers.npc.varrock

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

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(35) == 5) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
