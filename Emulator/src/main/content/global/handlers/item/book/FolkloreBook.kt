package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class FolkloreBook : InteractionListener {
    /*
     * Book found on the  bookshelves in the Wise Old Man's
     * house in Draynor Village.
     * Sources: https://youtu.be/b2lzW1rA_5E?si=qoE0ug7OmaNZgTB1&t=257
     */

    companion object {
        private const val TITLE = "The Myth of the Elder-dragons"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Although the Elder-", 55),
                        BookLine("dragons are generally", 56),
                        BookLine("considered to be a myth,", 57),
                        BookLine("stories tell of an ancient", 58),
                        BookLine("race that dwelton", 59),
                        BookLine("Gielinor before any other.", 60),
                        BookLine("In some arcane way their", 61),
                        BookLine("lifeforce was linked to the", 62),
                        BookLine("life of the world itself; it", 63),
                        BookLine("is written that, as long as", 64),
                        BookLine("the Elder-dragons", 65),
                    ),
                    Page(
                        BookLine("continue to live, the world", 66),
                        BookLine("shall not end.", 67),
                        BookLine("", 68),
                        BookLine("However, sceptics claim", 69),
                        BookLine("that the myth of the", 70),
                        BookLine("Elder-dragons was", 71),
                        BookLine("invented to cast doubt on", 72),
                        BookLine("claims that this realm was", 73),
                        BookLine("lifeless until the gods first", 74),
                        BookLine("arrived.", 75),
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
        on(Items.BOOK_OF_FOLKLORE_5508, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
