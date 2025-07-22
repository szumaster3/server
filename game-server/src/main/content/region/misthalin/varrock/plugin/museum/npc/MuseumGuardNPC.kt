package content.region.misthalin.varrock.plugin.museum.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Museum Guard NPC.
 */
class MuseumGuardNPC : NPCBehavior(*guardsNPCs) {
    companion object {
        private val guardsNPCs = intArrayOf(
            NPCs.MUSEUM_GUARD_5941,
            NPCs.MUSEUM_GUARD_5942,
            NPCs.MUSEUM_GUARD_5943,
        )
    }

    private val forceChat = arrayOf(
        "Another boring day.",
        "Nothing new there.",
        "Keep 'em coming!",
        "Don't daudle there!",
    )

    override fun onCreation(self: NPC) {
        self.isNeverWalks = false
        self.isWalks = false
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
