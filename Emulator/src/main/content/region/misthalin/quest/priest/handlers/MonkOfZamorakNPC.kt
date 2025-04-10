package content.region.misthalin.quest.priest.handlers

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class MonkOfZamorakNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = MonkOfZamorakNPC(id, location)

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            val player = killer.asPlayer()
            val quest = player.getQuestRepository().getQuest(Quests.PRIEST_IN_PERIL)
            if (quest.isStarted(player) && player.viewport.region.regionId == 13662) {
                GroundItemManager.create(Item(Items.GOLDEN_KEY_2944, 1), getLocation(), player)
            }
        }
        super.finalizeDeath(killer)
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.MONK_OF_ZAMORAK_1046, NPCs.MONK_OF_ZAMORAK_1045, NPCs.MONK_OF_ZAMORAK_1044)
}
