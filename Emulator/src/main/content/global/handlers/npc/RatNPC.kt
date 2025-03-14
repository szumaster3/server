package content.global.handlers.npc

import core.api.quest.getQuest
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
class RatNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return RatNPC(id, location)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            if (getQuest(killer, Quests.WITCHS_POTION).isStarted(killer.asPlayer())) {
                GroundItemManager.create(RAT_TAIL, getLocation(), killer.asPlayer())
            }
        }
    }

    override fun getIds(): IntArray {
        return ID
    }

    companion object {
        private val ID =
            intArrayOf(
                NPCs.RAT_47,
                NPCs.RAT_2682,
                NPCs.RAT_2980,
                NPCs.RAT_2981,
                NPCs.RAT_3007,
                NPCs.RAT_3008,
                NPCs.RAT_3009,
                NPCs.RAT_3010,
                NPCs.RAT_3011,
                NPCs.RAT_3012,
                NPCs.RAT_3013,
                NPCs.RAT_3014,
                NPCs.RAT_3015,
                NPCs.RAT_3016,
                NPCs.RAT_3017,
                NPCs.RAT_3018,
                NPCs.RAT_4396,
                NPCs.RAT_4415,
                NPCs.RAT_7202,
                NPCs.RAT_7204,
                NPCs.RAT_7417,
                NPCs.RAT_7461,
            )
        private val RAT_TAIL = Item(Items.RATS_TAIL_300, 1)
    }
}
