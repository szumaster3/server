package content.region.misthalin.varrock.plugin.museum

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class MuseumGuardNPC : NPCBehavior(NPCs.MUSEUM_GUARD_5941, NPCs.MUSEUM_GUARD_5942, NPCs.MUSEUM_GUARD_5943) {

    private val forceChat = arrayOf(
        "Another boring day.",
        "Nothing new there.",
        "Keep 'em coming!",
        "Don't daudle there!"
    )

    override fun onCreation(self: NPC) {
        self.isNeverWalks = false
        self.isWalks = false
    }

    private var nextChat = 0L

    override fun tick(self: NPC): Boolean {
        val now = System.currentTimeMillis()
        if (now < nextChat || !self.isPlayerNearby(15)) {
            return true
        }

        if (RandomFunction.roll(8)) {
            sendChat(self, forceChat.random())
            nextChat = now + 5000L
        }

        return true
    }
}
