package content.region.kandarin.quest.grandtree.handlers

import content.region.kandarin.quest.grandtree.dialogue.ForemanGTDialogue
import core.api.*
import core.api.item.produceGroundItem
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ForemanNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location),
    InteractionListener {
    override fun construct(
        id: Int,
        location: Location?,
        vararg objects: Any?,
    ): AbstractNPC {
        return ForemanNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FOREMAN_674)
    }

    override fun defineListeners() {
        on(this.ids, IntType.NPC, "talk-to") { player, npc ->
            if (!isQuestComplete(player, Quests.THE_GRAND_TREE)) {
                openDialogue(player, ForemanGTDialogue(), npc)
            } else {
                sendDialogue(player, "The foreman is too busy to talk.")
            }
            return@on true
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (getQuestStage(killer as Player, Quests.THE_GRAND_TREE) == 55) {
            sendMessage(killer, "The foreman drops a piece of paper as he dies.")
            produceGroundItem(killer, Items.LUMBER_ORDER_787, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}
