package content.global.plugins.interaction.with_obj

import core.api.Container
import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Items
import shared.consts.Scenery

class SackOnHayPlugin : InteractionListener {

    companion object {
        private val hayScenery = intArrayOf(Scenery.HAY_BALE_36892, Scenery.HAY_BALES_36894, Scenery.HAY_BALES_36896, Scenery.HAYSTACK_300, Scenery.HAY_BALES_34593, Scenery.HAY_BALES_298, Scenery.HAY_BALES_299)
    }

    override fun defineListeners() {

        /*
         * Handles use empty sack on hay.
         */

        onUseWith(IntType.SCENERY, Items.EMPTY_SACK_5418, *hayScenery) { player, used, _ ->
            if (removeItem(player, used.asItem(), Container.INVENTORY)) {
                addItem(player, Items.HAY_SACK_6057, 1)
                sendMessage(player, "You fill the sack with hay.")
            }
            return@onUseWith true
        }
    }
}
