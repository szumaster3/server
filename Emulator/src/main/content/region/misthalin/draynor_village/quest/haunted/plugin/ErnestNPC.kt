package content.region.misthalin.draynor_village.quest.haunted.plugin

import core.api.quest.isQuestComplete
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the abstract NPC class to handle Ernest the Chicken.
 */
class ErnestNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {

    companion object {
        /**
         * The NPC ids of NPCs using this plugin.
         */
        private val ID = intArrayOf(NPCs.ERNEST_287)
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any?,
    ): AbstractNPC = ErnestNPC(id, location)

    override fun isHidden(player: Player): Boolean {
        val target: Player? = getAttribute("target", null)

        if (target == null || player != target) {
            return true
        }

        if (isQuestComplete(player, Quests.ERNEST_THE_CHICKEN)) {
            clear()
            return super.isHidden(player)
        }

        return false
    }

    override fun getIds(): IntArray = ID
}