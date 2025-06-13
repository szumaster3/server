package content.region.morytania.port_phasmatys.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs
import kotlin.random.Random

class GravingasNPC : NPCBehavior(NPCs.GRAVINGAS_1685) {

    private val chats = arrayOf(
        "Down with Necrovaus!!",
        "Rise up my fellow ghosts, and we shall be victorious!",
        "Power to the Ghosts!!",
        "Rise together, Ghosts without a cause!!",
        "United we conquer - divided we fall!!",
        "We shall overcome!!",
        "Let Necrovarus know we want out!!",
        "Don't stay silent - victory in numbers!!"
    )

    private var tickDelay = randomTickDelay()

    override fun tick(self: NPC): Boolean {
        if (!self.isPlayerNearby(10)) return true

        if (--tickDelay <= 0) {
            sendChat(self, chats.random())
            tickDelay = randomTickDelay()
        }

        return true
    }

    private fun randomTickDelay(): Int {
        return Random.nextInt(20, 50)
    }
}
