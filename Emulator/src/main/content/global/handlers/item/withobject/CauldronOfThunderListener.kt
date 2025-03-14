package content.global.handlers.item.withobject

import core.api.addItem
import core.api.animate
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class CauldronOfThunderListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles interaction with cauldron of thunder.
         * The cauldron of thunder was missing its animation.
         * 3 March 2009 (Update): "render_anim": "3939"
         */
        onUseWith(IntType.SCENERY, ITEM_MAP.keys.toIntArray(), Scenery.CAULDRON_OF_THUNDER_2142) { player, used, _ ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            animate(player, Animations.HUMAN_WITHDRAW_833)
            addItem(player, ITEM_MAP[used.id]!!)
            sendMessage(player, "You dip the " + used.name.lowercase() + " in the cauldron.")
            return@onUseWith true
        }
    }

    companion object {
        private val ITEM_MAP =
            mapOf(
                Items.RAW_BEEF_2132 to Items.ENCHANTED_BEEF_522,
                Items.RAW_RAT_MEAT_2134 to Items.ENCHANTED_RAT_MEAT_523,
                Items.RAW_BEAR_MEAT_2136 to Items.ENCHANTED_BEAR_MEAT_524,
                Items.RAW_CHICKEN_2138 to Items.ENCHANTED_CHICKEN_525,
            )
    }
}
