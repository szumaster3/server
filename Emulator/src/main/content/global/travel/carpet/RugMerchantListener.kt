package content.global.travel.carpet

import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener

class RugMerchantListener : InteractionListener {
    private val rugMerchants = intArrayOf(2291, 2292, 2293, 2294, 2296, 2298, 3020)

    override fun defineListeners() {
        /*
         * Handles travel option for Rug Merchants NPC.
         */

        on(rugMerchants, IntType.NPC, "travel") { player, node ->
            openDialogue(player, node.id, node, true, true)
            return@on true
        }
    }
}
