package content.global.plugin.item.withitem

import core.api.addItem
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class CarvedEvilTurnipPlugin : InteractionListener {

    private val evilTurnip = Items.EVIL_TURNIP_12134
    private val carvedEvilTurnip = Items.CARVED_EVIL_TURNIP_12153

    override fun defineListeners() {

        /*
         * Handles use knife with evil turnip.
         */

        onUseWith(IntType.ITEM, evilTurnip, Items.KNIFE_946) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendMessage(player, "You carve a scary face into the evil turnip.")
                sendMessage(player, "Wooo! It's enough to give you nightmares.")
                return@onUseWith addItem(player, carvedEvilTurnip)
            }
            return@onUseWith false
        }
    }
}
