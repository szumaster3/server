package content.region.misthalin.quest.haunted.handlers

import core.api.quest.isQuestComplete
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ErnestNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC = ErnestNPC(id, location)

    override fun isHidden(player: Player): Boolean {
        val target: Player? = getAttribute("target", null)
        if (player != target) {
            return true
        }
        if (player == target && isQuestComplete(target, Quests.ERNEST_THE_CHICKEN)) {
            clear()
            super.isHidden(player)
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ERNEST_287)
}
