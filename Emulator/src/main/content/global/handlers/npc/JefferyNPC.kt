package content.global.handlers.npc

import core.api.inBorders
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs

private val FURNACE_AREA = ZoneBorders(3104, 3498, 3108, 3502)

class JefferyNPC : NPCBehavior(NPCs.JEFFERY_6298) {
    override fun onCreation(self: NPC) {
        if (inBorders(self, FURNACE_AREA)) {
            val movementPath =
                arrayOf(
                    Location.create(3104, 3500, 0),
                    Location.create(3105, 3498, 0),
                    Location.create(3107, 3502, 0),
                    Location.create(3106, 3502, 0),
                    Location.create(3106, 3500, 0),
                )
            self.configureMovementPath(*movementPath)
            self.isWalks = true
        }
    }
}
