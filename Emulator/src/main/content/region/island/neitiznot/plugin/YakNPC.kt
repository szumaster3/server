package content.region.island.neitiznot.plugin

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class YakNPC : NPCBehavior(NPCs.YAK_5529) {
    override fun tick(self: NPC): Boolean {
        if (RandomFunction.roll(20)) {
            sendChat(self, "Moo")
        }
        return super.tick(self)
    }
}
