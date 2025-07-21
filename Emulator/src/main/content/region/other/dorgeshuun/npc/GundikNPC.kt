package content.region.other.dorgeshuun.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

/**
 * Handles the Gundik NPC.
 */
class GundikNPC : NPCBehavior(NPCs.GUNDIK_5796) {
    private var ticks = 0
    private val forceChat =
        arrayOf(
            "Spicy kebabs!",
            "Bat kebabs!",
            "Spicy bat kebabs!",
            "Bat shish kebabs!",
            "Kebabs!",
        )

    override fun tick(self: NPC): Boolean {
        ticks++
        if (ticks < 20) return true

        ticks = 0
        if (RandomFunction.random(100) < 15) {
            sendChat(self, forceChat.random())
        }
        return super.tick(self)
    }
}
