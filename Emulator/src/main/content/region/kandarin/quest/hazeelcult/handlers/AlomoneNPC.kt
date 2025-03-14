package content.region.kandarin.quest.hazeelcult.handlers

import content.region.kandarin.quest.hazeelcult.handlers.HazeelCultListener.Companion.ALOMONE
import core.api.item.produceGroundItem
import core.api.quest.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AlomoneNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return AlomoneNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALOMONE_891)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        ALOMONE.respawnTick = 60
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (isQuestInProgress(killer, "Hazeel Cult", 3, 4)) {
                produceGroundItem(killer, Items.CARNILLEAN_ARMOUR_2405, 1, this.location)
            }
            super.finalizeDeath(killer)
        }
    }
}
