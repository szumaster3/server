package content.region.misc.handlers.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class TradeRefeeNPC : NPCBehavior(NPCs.TRADE_REFEREE_2127) {
    private val forceChat =
        arrayOf(
            "Stay inside your triangle!",
            "Who's next?",
            "Next bid please!",
            "Need an offer, now!",
            "Keep the goods flowing!",
            "Keep it civil!",
            "Come on, come on!",
            "Go again please.",
            "Keep the goods flowing!",
            "Hold on there!",
        )

    override fun onCreation(self: NPC) {
        self.walkRadius = 6
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(35) == 5) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
