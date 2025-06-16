package content.region.asgarnia.goblinvillage.quest.gobdip.plugin

import core.api.sendItemDialogue
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld.ticks
import org.rs.consts.Items
import org.rs.consts.Scenery

class GoblinDiplomacyPlugin : InteractionListener {

    companion object {
        private val GOBLIN_MAIL = Item(Items.GOBLIN_MAIL_288)
        private val CRATES = intArrayOf(Scenery.CRATE_16561, Scenery.CRATE_16560, Scenery.CRATE_16559)
    }

    override fun defineListeners() {
        for (crateId in CRATES) {
            on(crateId, IntType.SCENERY, "search") { player, _ ->
                if (player.getAttribute("crate:$crateId", 0) < ticks) {
                    setAttribute(player, "crate:$crateId", ticks + 500)
                    if (!player.inventory.add(GOBLIN_MAIL)) {
                        GroundItemManager.create(GOBLIN_MAIL, player)
                    }
                    sendItemDialogue(player, GOBLIN_MAIL.id, "You find some goblin armour.")
                } else {
                    sendMessage(player, "You search the crate but find nothing.")
                }
                return@on true
            }
        }
    }
}
