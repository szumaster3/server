package content.region.kandarin.ardougne.quest.tol.plugin

import core.api.getWorldTicks
import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import org.rs.consts.NPCs

class GunsNPC : NPCBehavior(NPCs.THE_GUNS_5592) {
    private var lifts = 0

    override fun onCreation(self: NPC) {
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean {
        if (getWorldTicks() % 3 == 0) {
            sendChat(self, lifts++.toString())
        }
        if (lifts > 5000) {
            lifts = 0
        }
        return true
    }
}
