package content.region.fremennik.quest.olaf.handlers

import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class OlafsQuestListener : InteractionListener {
    override fun defineListeners() {
        val treasureMap = Items.SVENS_LAST_MAP_11034

        on(treasureMap, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.OLAF2_TREASUREMAP_254)
            return@on true
        }
    }
}
