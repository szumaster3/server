package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class MurkyMattNPC : NPCBehavior(NPCs.MURKY_MATT_RUNES_6525) {
    private val forceChat = arrayOf(
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

    private var nextChat = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextChat || !self.isPlayerNearby(15)) {
            return true
        }

        if (RandomFunction.roll(8)) {
            sendChat(self, forceChat.random())
            nextChat = now + 15000L
        }

        return true
    }
}
