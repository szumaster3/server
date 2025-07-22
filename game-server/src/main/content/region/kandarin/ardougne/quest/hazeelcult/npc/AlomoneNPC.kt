package content.region.kandarin.ardougne.quest.hazeelcult.npc

import content.region.kandarin.ardougne.quest.hazeelcult.plugin.HazeelCultPlugin
import core.api.produceGroundItem
import core.api.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AlomoneNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = AlomoneNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALOMONE_891)

    override fun handleTickActions() {
        super.handleTickActions()
        HazeelCultPlugin.ALOMONE.respawnTick = 60
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (isQuestInProgress(killer, Quests.HAZEEL_CULT, 3, 4)) {
                produceGroundItem(killer, Items.CARNILLEAN_ARMOUR_2405, 1, this.location)
            }
            super.finalizeDeath(killer)
        }
    }
}
