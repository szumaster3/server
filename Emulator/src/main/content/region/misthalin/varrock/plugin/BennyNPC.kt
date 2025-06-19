package content.region.misthalin.varrock.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the BennyNPC.
 */
class BennyNPC : NPCBehavior(NPCs.BENNY_5925) {

    private val forceChat =
        arrayOf(
            "Read all about it!",
            "Varrock Herald, on sale here!",
            "Buy your Varrock Herald now!",
            "Extra! Extra! Read all about it!",
            "Varrock Herald, now only 50 gold!",
        )

    override fun onCreation(self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
        self.walkRadius = 6
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
