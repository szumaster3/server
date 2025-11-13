package content.region.kandarin.gnome_stronghold.quest.grandtree.npc

import core.api.getQuestStage
import core.api.produceGroundItem
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Foreman NPC.
 *
 * # Relations
 * [The Grand Tree][content.region.kandarin.gnome_stronghold.quest.grandtree.TheGrandTree]
 */
@Initializable
class ForemanNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC = ForemanNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.FOREMAN_674)

    override fun finalizeDeath(killer: Entity?) {
        if (getQuestStage(killer as Player, Quests.THE_GRAND_TREE) == 55) {
            sendMessage(killer, "The foreman drops a piece of paper as he dies.")
            produceGroundItem(killer, Items.LUMBER_ORDER_787, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}
