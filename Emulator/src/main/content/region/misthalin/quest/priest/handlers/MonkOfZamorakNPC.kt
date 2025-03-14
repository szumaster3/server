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
    ): AbstractNPC {
        return MonkOfZamorakNPC(id, location)
    }

    override fun tick() {
        super.tick()
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        val p = (killer as Player)
        val quest = p.getQuestRepository().getQuest(Quests.PRIEST_IN_PERIL)
        if (quest.isStarted(p)) {
            GroundItemManager.create(Item(Items.GOLDEN_KEY_2944, 1), getLocation(), p)
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MONK_OF_ZAMORAK_1046)
    }
}
