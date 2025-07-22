package content.region.kandarin.yanille.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class GuardCaptainNPC : NPCBehavior(NPCs.GUARD_CAPTAIN_3109) {
    private var ticks = 0
    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 10) return true
        ticks = 0

        if (RandomFunction.random(15, 100) == 5) {
            sendChat(self, "*HIC*")
        }
        return true
    }
}

