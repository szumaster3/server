package content.global.handlers.item.book

import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Items

class PortraitureBook : InteractionListener {
    override fun defineListeners() {
        on(Items.BOOK_OF_PORTRAITURE_4817, IntType.ITEM, "read") { player, _ ->
            sendItemDialogue(
                player,
                Items.BOOK_OF_PORTRAITURE_4817,
                "All interested artisans should really consider taking up the hobby of portraiture. To do so, one uses a piece of papyrus on the intended subject to initiate a likeness drawing activity.",
            )
            return@on true
        }
    }
}
