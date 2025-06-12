package content.region.other.keldagrim.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import org.rs.consts.NPCs
import kotlin.random.Random

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

    private var tickDelay = Random.nextInt(10, 20)

    override fun onCreation(self: NPC) {
        self.walkRadius = 6
    }

    override fun tick(self: NPC): Boolean {
        if (!isPlayerNearby(self)) return true

        if (--tickDelay > 0) return true
        tickDelay = Random.nextInt(10, 20)

        sendChat(self, forceChat.random())
        return true
    }
}
