package content.region.asgarnia.taverley.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Sylas NPC.
 */
class SylasNPC : NPCBehavior(NPCs.SYLAS_5987) {
    private var ticks = 0
    private val forceChat = arrayOf(
        "Selling Magic Beans!",
        "You there, you look like you could use some beans!",
        "All beans must go!",
        "Buy 1 bean get a bean free!",
        "Selling bags of Magic Beans!",
    )

    override fun onCreation(self: NPC) {
        self.walkRadius = 5
    }

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 20) {
            return true
        }
        ticks = 0

        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
