package content.global.handlers.item.scroll

import content.global.handlers.iface.ScrollInterface
import content.global.handlers.iface.ScrollLine
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs.consts.Components
import org.rs.consts.Items

class TodoScroll : InteractionListener {
    companion object {
        private const val SCROLL_INTERFACE = Components.MESSAGESCROLL_220
        val THE_TODO_LIST =
            arrayOf(
                ScrollLine("Things to do:", 3),
                ScrollLine("", 4),
                ScrollLine("Take out the rubbish", 5),
                ScrollLine("Buy a new record for the gramophone", 6),
                ScrollLine("Check shrunken ogleroot supply", 7),
                ScrollLine("Feed ogleroots to my darlings", 8),
                ScrollLine("Wash the bedsheets", 9),
                ScrollLine("Hang out the washing", 10),
                ScrollLine("Practice the piano", 11),
                ScrollLine("Terrorise the little brat with the ball again", 12),
                ScrollLine("Trim hedges in the garden", 13),
                ScrollLine("Floss warts", 14),
                ScrollLine("", 15),
                ScrollLine("Plot revenge on Fritz's murder", 16),
            )
    }

    override fun defineListeners() {
        on(Items.TO_DO_LIST_11203, IntType.ITEM, "read") { player, _ ->
            ScrollInterface.scrollSetup(player, SCROLL_INTERFACE, THE_TODO_LIST)
            return@on true
        }
    }
}
