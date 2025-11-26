package content.global.plugins.item

import core.api.getCharge
import core.api.sendMessage
import core.api.setCharge
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items

class ClayBraceletOptionPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles options interaction of clay bracelet.
         */

        on(Items.BRACELET_OF_CLAY_11074, IntType.ITEM, "operate") { player, node ->
            var charge = getCharge(node)
            if (charge > 28) setCharge(node, 28).also { charge = 28 }
            sendMessage(player, "You have $charge uses left.")
            return@on true
        }
    }
}
