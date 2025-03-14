package content.region.asgarnia.quest.troll.handlers.npc

import core.api.inInventory
import core.api.item.produceGroundItem
import core.api.quest.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class TrollGeneralsNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC {
        return TrollGeneralsNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TROLL_GENERAL_1115, NPCs.TROLL_GENERAL_1116, NPCs.TROLL_GENERAL_1117)
    }

    override fun finalizeDeath(killer: Entity?) {
        if (isQuestInProgress(killer as Player, Quests.TROLL_STRONGHOLD, 1, 7) &&
            !inInventory(
                killer,
                Items.PRISON_KEY_3135,
            )
        ) {
            produceGroundItem(killer, Items.PRISON_KEY_3135, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}
