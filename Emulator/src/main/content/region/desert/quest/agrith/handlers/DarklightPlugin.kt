package content.region.desert.quest.agrith.handlers

import core.api.addItem
import core.api.inInventory
import core.api.quest.hasRequirement
import core.api.removeItem
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items
import org.rs.consts.Quests

class DarklightPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles use the black dye on silverlight.
         */

        onUseWith(IntType.ITEM, Items.BLACK_MUSHROOM_INK_4622, Items.SILVERLIGHT_2402) { player, used, with ->
            if (!hasRequirement(player, Quests.SHADOW_OF_THE_STORM) || (!inInventory(player, Items.BLACK_MUSHROOM_INK_4622, 1) && (!inInventory(player, Items.SILVERLIGHT_2402, 1)))) {
                return@onUseWith false
            }
            if (removeItem(player, used.id) && removeItem(player, with.id)) {
                addItem(player, Items.DARKLIGHT_6746)
            }
            return@onUseWith true
        }
    }
}
