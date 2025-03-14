package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ClockWorkBook : InteractionListener {
    /*
     * Obtainable during the Cold War quest.
     */

    companion object {
        private const val TITLE = "A Clockwork Mechanism, Chapter 1.0"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Making a clockwork toy", 55),
                        BookLine("is a very simple and good", 57),
                        BookLine("place to start for new", 58),
                        BookLine("crafters. Simply create", 59),
                        BookLine("a clockwork mechanism", 60),
                        BookLine("on your Crafting table,", 61),
                        BookLine("then use it and a wood", 62),
                        BookLine("plank to make a toy", 63),
                        BookLine("soldier or doll. Wind", 64),
                        BookLine("them up and watch them", 65),
                    ),
                    Page(
                        BookLine("go! It would be simple", 66),
                        BookLine("to alter the design", 67),
                        BookLine("by adding a piece of", 68),
                        BookLine("silk to the wooden", 69),
                        BookLine("frame and clockwork", 70),
                        BookLine("mechanism. You could", 71),
                        BookLine("make a suit that", 72),
                        BookLine("could be controlled from", 73),
                        BookLine("the inside.", 74),
                    ),
                ),
            )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(
            player,
            BookInterface.FANCY_BOOK_3_49,
            TITLE,
            CONTENTS,
        )
        return true
    }

    override fun defineListeners() {
        on(Items.CLOCKWORK_BOOK_10594, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
