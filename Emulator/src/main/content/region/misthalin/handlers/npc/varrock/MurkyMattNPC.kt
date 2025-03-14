package content.region.misthalin.handlers.npc.varrock

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class MurkyMattNPC : NPCBehavior(NPCs.MURKY_MATT_RUNES_6525) {
    private val forceChat =
        arrayOf(
            "Sure be a busy place, today.",
            "Yarrr! I'm gonna be rich, I tell ye!",
            "I'm lovin' this Grand Exchange! Arrr!",
            "Arrr! Another good sale!",
            "No! Me prices, they be goin' down!",
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
