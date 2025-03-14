package content.region.kandarin.quest.arena.handlers.npc

import core.api.inBorders
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import org.rs.consts.NPCs

class KhazardBarmanNPC : NPCBehavior(NPCs.KHAZARD_BARMAN_259) {
    override fun onCreation(self: NPC) {
        if (inBorders(self, 2563, 3139, 2572, 3144)) {
            val movementPath = arrayOf(Location.create(2564, 3140, 0), Location.create(2569, 3140, 0))
            self.configureMovementPath(*movementPath)
            self.isWalks = true
        }
    }
}
