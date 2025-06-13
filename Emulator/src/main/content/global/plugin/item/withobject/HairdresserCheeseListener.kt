package content.global.plugin.item.withobject

import core.api.removeItem
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Scenery

class HairdresserCheeseListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles using cheese on treadmill. (Falador diaries)
         */

        onUseWith(IntType.SCENERY, Items.CHEESE_1985, Scenery.TREADMILL_11677) { player, used, _ ->
            if (removeItem(player, used)) {
                sendMessage(player, "You throw the cheese to Ridgeley, for which he appears grateful.")
                setAttribute(player, "diary:falador:feed-ridgeley-with-cheese", true)
            }
            return@onUseWith true
        }
    }
}
