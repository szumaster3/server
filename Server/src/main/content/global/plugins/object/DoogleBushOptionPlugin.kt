package content.global.plugins.`object`

import core.api.addItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Scenery

class DoogleBushOptionPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles picking leaf from doogle bush.
         */

        on(Scenery.DOOGLE_BUSH_31155, IntType.SCENERY, "pick-leaf") { player, _ ->
            if (!addItem(player, Items.DOOGLE_LEAVES_1573)) {
                sendMessage(player, "You don't have enough space in your inventory.")
            } else {
                sendMessage(player, "You pick some doogle leaves.")
            }
            return@on true
        }
    }
}
