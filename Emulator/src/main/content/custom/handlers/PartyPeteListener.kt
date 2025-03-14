package content.custom.handlers

import core.api.interaction.openNpcShop
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.NPCs

class PartyPeteListener : InteractionListener {

    override fun defineListeners() {
        /*
         * Handles Party Pete's Emporium shop.
         */
        on(NPCs.PARTY_PETE_659, IntType.NPC, "trade") { player, node ->
            openNpcShop(player, node.id)
            return@on true
        }
    }
}