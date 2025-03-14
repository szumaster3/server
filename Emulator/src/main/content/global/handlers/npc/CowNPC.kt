package content.global.handlers.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class CowNPC : NPCBehavior(NPCs.COW_81, NPCs.COW_397, NPCs.COW_955, NPCs.COW_CALF_1766, NPCs.COW_1767, NPCs.COW_3309) {
    override fun tick(self: NPC): Boolean {
        if (RandomFunction.roll(20) && self.id != NPCs.COW_CALF_1766) {
            sendChat(self, "Moo")
        }
        return true
    }
}
