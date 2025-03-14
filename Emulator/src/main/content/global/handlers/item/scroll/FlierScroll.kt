package content.global.handlers.item.scroll

import core.api.sendDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class FlierScroll : InteractionListener {
    override fun defineListeners() {
        on(Items.FLIER_956, IntType.ITEM, "read") { player, _ ->
            sendDialogue(
                player,
                "Buy from Bob's Brilliant Axes - if you see our prices bettered, let us know and we will best them. Offer subject to availability and at Bob's discretion. Does not affect your legal rights under Lumbridge laws.",
            )
            return@on true
        }
    }
}
