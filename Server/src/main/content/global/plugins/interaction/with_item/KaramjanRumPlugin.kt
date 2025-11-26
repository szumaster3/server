package content.global.plugins.interaction.with_item

import core.api.addItemOrDrop
import core.api.animate
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import shared.consts.Animations
import shared.consts.Items

class KaramjanRumPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles creating Karamjan rum (banana version).
         */

        onUseWith(IntType.ITEM, Items.SLICED_BANANA_3162, Items.KARAMJAN_RUM_431) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                animate(player, Animations.BANANA_RUM_1195)
                sendMessage(player, "You add the banana slices to the Karamjan rum.")
                addItemOrDrop(player, Items.KARAMJAN_RUM_3164, 1)
                return@onUseWith true
            }
            return@onUseWith false
        }

        /*
         * Handles using the banana with karamjan rum.
         */

        onUseWith(IntType.ITEM, Items.BANANA_1963, Items.KARAMJAN_RUM_431) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                animate(player, Animations.BANANA_RUM_1195)
                sendMessage(player, "You stuff the banana into the neck of the bottle. You begin to wonder why.")
                addItemOrDrop(player, Items.KARAMJAN_RUM_3165, 1)
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}
