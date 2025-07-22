package content.region.morytania.phas.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class GravingasNPC : NPCBehavior(NPCs.GRAVINGAS_1685) {
    private var timer = 0L
    private val forceChat =
        arrayOf(
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
        val now = System.currentTimeMillis()
        if (now < timer) return true
        if (RandomFunction.random(10, 100) == 5) {
            sendChat(self, forceChat.random())
            timer = now + 3000
        }
        return true
    }
}
