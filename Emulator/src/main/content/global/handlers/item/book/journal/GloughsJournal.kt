package content.global.handlers.item.book.journal

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import org.rs.consts.Items

class GloughsJournal : InteractionListener {
    /*
     * Glough's journal is found during The Grand Tree quest and looks
     * a little deeper into the mind and motives of Glough the gnome.
     * It's used as a key piece of evidence as to what he's planning.
     */

    companion object {
        private const val TITLE = "Glough's Journal"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=FF2D00>The migration failed!</col>", 55),
                        BookLine("After spending half a", 56),
                        BookLine("century hiding underground", 57),
                        BookLine("you would think that the", 58),
                        BookLine("great migration would", 59),
                        BookLine("have improved life on", 60),
                        BookLine("" + settings!!.name + " for tree gnomes.", 61),
                        BookLine("However, rather than the", 62),
                        BookLine("great liberation promised", 63),
                        BookLine("to us by King Healthorg", 64),
                        BookLine("at the end of the last", 65),
                    ),
                    Page(
                        BookLine("age, we have been forced", 66),
                        BookLine("to live in hiding up trees", 67),
                        BookLine("or in the gnome maze,", 68),
                        BookLine("laughed at and mocked", 69),
                        BookLine("by man. Living in constant", 70),
                        BookLine("fear of human aggression,", 71),
                        BookLine("we are in a no better", 72),
                        BookLine("situation now than when", 73),
                        BookLine("we lived in the caves!", 74),
                        BookLine("Change must come soon!", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=FF2D00>They must be stopped!</col>", 55),
                        BookLine("Today I heard of three", 56),
                        BookLine("more gnomes slain by", 57),
                        BookLine("Khazard's human troops", 58),
                        BookLine("for fun, I can't control", 59),
                        BookLine("my anger! Humanity seems", 60),
                        BookLine("to have acquired a level", 61),
                        BookLine("of arrogance comparable", 62),
                        BookLine("to that of Zamorak, killing", 63),
                        BookLine("and pillaging at will!", 64),
                        BookLine("We are small and at heart", 65),
                    ),
                    Page(
                        BookLine("not warriors but something", 66),
                        BookLine("must be done! We will", 67),
                        BookLine("pick up arms and go forth", 68),
                        BookLine("into the human world!", 69),
                        BookLine("We will defend ourselves", 70),
                        BookLine("and we will pursue justice", 71),
                        BookLine("for all gnomes who fell", 72),
                        BookLine("at the hands of humans!", 73),
                        BookLine("", 74),
                        BookLine("<col=FF2D00>Gaining Support</col>", 75),
                        BookLine("Some of the local gnomes", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("seem strangely deluded", 55),
                        BookLine("about humans, many believe", 56),
                        BookLine("that humans are not all", 57),
                        BookLine("naturally evil but instead", 58),
                        BookLine("vary from person to person.", 59),
                        BookLine("This sort of talk could", 60),
                        BookLine("be the end for the tree", 61),
                        BookLine("gnomes and I must continue", 62),
                        BookLine("to convince my fellow", 63),
                        BookLine("gnome folk the cold truth", 64),
                        BookLine("about these human creatures!", 65),
                    ),
                    Page(
                        BookLine("How they will not stop", 66),
                        BookLine("until all gnome life", 67),
                        BookLine("is destroyed! Unless", 68),
                        BookLine("we can destroy them first!", 69),
                    ),
                ),
            )

        @Suppress("UNUSED_PARAMETER")
        private fun display(
            player: Player,
            pageNum: Int,
            buttonID: Int,
        ): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.GLOUGHS_JOURNAL_785, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
            return@on true
        }
    }
}
