package content.region.misthalin.handlers.npc.varrock

import core.api.visualize
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class ChurchNPC : NPCBehavior(*sleepyNPC) {
    companion object {
        private val sleepyNPC = intArrayOf(NPCs.MARTINA_SCORSBY_3326, NPCs.JEREMY_CLERKSIN_3327)
    }

    override fun onCreation(self: NPC) {
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(35) == 5) {
            visualize(self, -1, 1056)
        }
        return true
    }
}
