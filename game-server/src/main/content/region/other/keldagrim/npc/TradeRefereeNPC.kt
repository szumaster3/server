package content.region.other.keldagrim.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the CartConductorNPC.
 */

class TradeRefereeNPC : NPCBehavior(NPCs.TRADE_REFEREE_2127) {
    private var ticks = 0
    private val forceChat = arrayOf(
        "Stay inside your triangle!",
        "Who's next?",
        "Next bid please!",
        "Need an offer, now!",
        "Keep the goods flowing!",
        "Keep it civil!",
        "Come on, come on!",
        "Go again please.",
        "Keep the goods flowing!",
        "Hold on there!"
    )

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 20) return true
        ticks = 0

        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
