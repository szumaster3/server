package content.region.misthalin.handlers.npc.dorgeshuun

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class MiltogNPC : NPCBehavior(NPCs.MILTOG_5781) {
    private val forceChat =
        arrayOf(
            "Lamps!",
            "Lanterns!",
            "Tinderboxes!",
            "Torches!",
            "Lamp oil!",
        )

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(35) == 5) {
            sendChat(self, forceChat.random())
        }
        return super.tick(self)
    }
}
