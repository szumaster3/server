package content.region.asgarnia.burthope.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Lidio NPC.
 */
class LidioNPC  : NPCBehavior(NPCs.LIDIO_4293) {
    var ticks = 0
    private val forceChat =
        arrayOf(
            "Potatoes are filling and healthy too!",
            "Come try my lovely pizza or maybe some fish!",
            "Stew to fill the belly, on sale here!"
        )

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 20) return true

        ticks = 0
        if (RandomFunction.random(100) < 3) {
            sendChat(self, forceChat.random())
        }
        return super.tick(self)
    }
}