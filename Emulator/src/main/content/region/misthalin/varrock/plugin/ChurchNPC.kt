package content.region.misthalin.varrock.plugin

import core.api.visualize
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class ChurchNPC : NPCBehavior(*ID) {

    override fun onCreation(self: NPC) {
        self.isWalks = false
    }

    private var nextSnore = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextSnore || !self.isPlayerNearby(15)) {
            return true
        }

        if (RandomFunction.roll(8)) {
            visualize(self, -1, 1056)
            nextSnore = now + 9999L
        }

        return true
    }

    companion object {
        private val ID = intArrayOf(NPCs.MARTINA_SCORSBY_3326, NPCs.JEREMY_CLERKSIN_3327)
    }
}
