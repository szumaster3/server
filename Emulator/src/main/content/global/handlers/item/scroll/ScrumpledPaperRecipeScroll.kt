package content.global.handlers.item.scroll

import content.global.handlers.iface.ScrollInterface
import content.global.handlers.iface.ScrollLine
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class ScrumpledPaperRecipeScroll : InteractionListener {
    companion object {
        private const val SCROLL_INTERFACE = Components.MESSAGESCROLL_220
        val SCRUMBLED_PAPER_TEXT =
            arrayOf(
                ScrollLine("*** Delicious Ugthanki kebab ***", 3),
                ScrollLine("", 4),
                ScrollLine("Ingredients:", 5),
                ScrollLine("Cooked Ugtkanki meat, Flour, Water, Onion, Tomato.", 6),
                ScrollLine("The Ugthanki meat should be nicely grilled.", 7),
                ScrollLine("Next take the flour and water and make some Pitta bread.", 8),
                ScrollLine("You'll need a range to do this.", 9),
                ScrollLine("Take an onion and chop it into a bowl.", 10),
                ScrollLine("Take a tomato and chop it into the onion mixture.", 11),
                ScrollLine("Chop the meat into the Onion and Tomato mixture.", 12),
                ScrollLine("Finally fill the pitta bread with the Ugthanki,", 13),
                ScrollLine("Onion and Tomato mixture to make your delicious Ugthanki", 14),
                ScrollLine("Kebab.", 15),
            )
    }

    override fun defineListeners() {
        on(Items.SCRUMPLED_PAPER_1847, IntType.ITEM, "read") { player, _ ->
            ScrollInterface.scrollSetup(player, SCROLL_INTERFACE, SCRUMBLED_PAPER_TEXT)
            return@on true
        }
    }
}
