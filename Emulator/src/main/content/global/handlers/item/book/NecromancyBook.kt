package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.getAttribute
import core.api.sendItemDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class NecromancyBook : InteractionListener {
    /*
     * The necromancy book is used during the Zogre Flesh Eaters quest.
     * It is found in the room of Sithik Ints, in the cupboards. Using
     * the Torn page on the book reveals that it comes from this book.
     */

    companion object {
        private const val TITLE = "necromancy book"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("This book uses very", 55),
                        BookLine("strange language and", 56),
                        BookLine("some incomprehensible", 57),
                        BookLine("symbols. It has a very", 58),
                        BookLine("dark and evil feeling", 59),
                        BookLine("to it. As you're looking", 60),
                        BookLine("through the book, you", 61),
                        BookLine("notice that one of the", 62),
                        BookLine("pages has been torn and", 63),
                        BookLine("half of it is missing.", 64),
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
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.NECROMANCY_BOOK_4837, IntType.ITEM, "read") { player, _ ->
            if (getAttribute(player, "zfe:missed-page", false)) {
                BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            } else {
                sendItemDialogue(
                    player,
                    Items.NECROMANCY_BOOK_4837,
                    "This book uses very strange language and some incomprehensible symbols. It has a very dark feeling to it. As you're looking through the book, you notice that one of the pages has been torn and half of it is missing.",
                )
            }
            return@on true
        }
    }
}
