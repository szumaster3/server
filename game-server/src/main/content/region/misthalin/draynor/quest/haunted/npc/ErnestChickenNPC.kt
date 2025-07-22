package content.region.misthalin.draynor.quest.haunted.npc

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the abstract npc class to handle Ernest the Chicken.
 *
 * @author Vexia
 */
class ErnestChickenNPC(
    id: Int = 0,
    location: Location? = null
) : AbstractNPC(id, location, false) {

    companion object {
        /**
         * The NPC ids of NPCs using this plugin.
         */
        private val ID = intArrayOf(NPCs.CHICKEN_288)
    }

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return ErnestChickenNPC(id, location)
    }

    override fun isHidden(player: Player): Boolean {
        return player.questRepository.getQuest(Quests.ERNEST_THE_CHICKEN).getStage(player) == 100 ||
                player.getAttribute("ernest-hide", false)
    }

    override fun getIds(): IntArray {
        return ID
    }
}