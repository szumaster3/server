package content.region.kandarin.gnome.plugin

import core.api.inBorders
import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Gnome Coach NPC.
 */
class GnomeCoachNPC : NPCBehavior(NPCs.GNOME_COACH_2802) {
    private val forceChat =
        arrayOf(
            "One-Two, One-Two!",
            "Run faster! Don't be so lazy!",
            "Pass to your left! Pass now!",
            "There are others to pass to you know",
            "Hey human, get moving!",
            "Take him out you wuss",
        )

    override fun onCreation(self: NPC) {
        if (inBorders(self, 2386, 3496, 2410, 3499)) {
            val movementPath =
                arrayOf(
                    Location.create(2392, 3498, 0),
                    Location.create(2398, 3498, 0),
                    Location.create(2403, 3498, 0),
                    Location.create(2397, 3498, 0),
                    Location.create(2391, 3498, 0),
                )
            self.configureMovementPath(*movementPath)
            self.isWalks = true
        }
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
