package content.region.desert.al_kharid.plugin

import core.api.openDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.NPCs

class RugMerchantPlugin : InteractionListener {

    private val rugMerchants = intArrayOf(NPCs.RUG_MERCHANT_2291, NPCs.RUG_MERCHANT_2292, NPCs.RUG_MERCHANT_2293, NPCs.RUG_MERCHANT_2294, NPCs.RUG_MERCHANT_2296, NPCs.RUG_MERCHANT_2298, NPCs.RUG_MERCHANT_3020)

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
