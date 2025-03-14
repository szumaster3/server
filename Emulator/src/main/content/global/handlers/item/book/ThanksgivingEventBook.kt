package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ThanksgivingEventBook : InteractionListener {
    /*
     * The turkey book is a book that players could obtain as part of the
     * 2008, 2014, 2015, and 2016 Thanksgiving events. It gives clues on
     * where to find disguised turkeys around Gielinor, which players could
     * find and spy on. Spying on all ten during the events unlocked the Give
     * Thanks emote. Players could obtain the book from the cook's brother in
     * Lumbridge Castle. The book itself gives no instructions, and players must
     * rely on dialogue from the cook's brother to know what the locations
     * mean and what they should look for.
     */

    companion object {
        private const val TITLE = "turkey book"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Turkey 1, location:", 56),
                        BookLine("near a mine filled", 57),
                        BookLine("with scorpions. Turkey", 58),
                        BookLine("2, location: a city", 59),
                        BookLine("in the desert. Turkey", 60),
                        BookLine("3, location: near", 61),
                        BookLine("the river Lum. Turkey", 62),
                        BookLine("4, location: in a", 63),
                        BookLine("swamp behind a castle. Turkey", 64),
                        BookLine("5, location: near", 65),
                    ),
                    Page(
                        BookLine("a vampyre-infested", 66),
                        BookLine("manor. Turkey 6, location:", 67),
                        BookLine("in the city of White", 68),
                        BookLine("Knights. Turkey 7,", 69),
                        BookLine("location: in a village", 70),
                        BookLine("near the edge of the", 71),
                        BookLine("Wilderness. Turkey", 72),
                        BookLine("8, location: somewhere", 73),
                        BookLine("near Varrock. Turkey", 74),
                        BookLine("9, location: near", 75),
                        BookLine("Mudskipper Point. Turkey", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("10, location: near", 55),
                        BookLine("the Monastery.", 56),
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
        on(Items.TURKEY_BOOK_14536, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
