package content.region.kandarin.handlers.npc.yanille

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class GuardCaptainNPC : NPCBehavior(NPCs.GUARD_CAPTAIN_3109) {
    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(1, 15) == 5) {
            sendChat(self, "*HIC*")
        }
        return true
    }
}
