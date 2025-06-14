package content.region.asgarnia.burthope.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class LidioNPC  : NPCBehavior(NPCs.LIDIO_4293) {
    private val forceChat =
        arrayOf(
            "Potatoes are filling and healthy too!",
            "Come try my lovely pizza or maybe some fish!",
            "Stew to fill the belly, on sale here!"
        )

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(100) == 5) {
            sendChat(self, forceChat.random())
        }
        return super.tick(self)
    }
}