package content.region.misthalin.quest.haunted.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ErnestChickenNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = ErnestChickenNPC(id, location)

    override fun isHidden(player: Player): Boolean {
        val questStage = player.getQuestRepository().getQuest(Quests.ERNEST_THE_CHICKEN).getStage(player)
        return questStage == 100 || player.getAttribute("ernest-hide", false)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CHICKEN_288)
}
