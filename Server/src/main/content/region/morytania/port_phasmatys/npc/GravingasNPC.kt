package content.region.morytania.port_phasmatys.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import shared.consts.NPCs

class GravingasNPC : NPCBehavior(NPCs.GRAVINGAS_1685) {

    companion object {
        private const val TICK_INTERVAL = 15
        private const val CHANCE_TO_TALK = 2
    }

    private var ticksToNextChat = RandomFunction.random(TICK_INTERVAL)

    private val forceChat = listOf(
        "Down with Necrovaus!!",
        "Rise up my fellow ghosts, and we shall be victorious!",
        "Power to the Ghosts!!",
        "Rise together, Ghosts without a cause!!",
        "United we conquer - divided we fall!!",
        "We shall overcome!!",
        "Let Necrovarus know we want out!!",
        "Don't stay silent - victory in numbers!!",
    )

    override fun tick(self: NPC): Boolean {
        if (--ticksToNextChat > 0) return true

        ticksToNextChat = TICK_INTERVAL

        if (RandomFunction.roll(CHANCE_TO_TALK)) {
            sendChat(self, forceChat.random())
        }

        return true
    }
}
