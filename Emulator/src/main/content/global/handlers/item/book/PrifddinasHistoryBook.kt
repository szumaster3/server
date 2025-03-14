package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class PrifddinasHistoryBook : InteractionListener {
    companion object {
        private const val TITLE = "The Creation of Prifddinas"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("It is told that the first elves", 55),
                        BookLine("elves to come through the", 56),
                        BookLine("World Gate found a world full", 57),
                        BookLine("of beautiful woodlands and", 58),
                        BookLine("terrible beasts. It was Seren", 59),
                        BookLine("who told the eight clans that", 60),
                        BookLine("if they were to survive in", 61),
                        BookLine("this hostile environment they", 62),
                        BookLine("must form a single community", 63),
                        BookLine("for mutual protection.", 64),
                    ),
                    Page(
                        BookLine("Seren proceeded to show them", 66),
                        BookLine("all to a clearing in the forest", 67),
                        BookLine("where she had already created", 68),
                        BookLine("the Tower of Voices, a place for", 69),
                        BookLine("for all elven kind to", 70),
                        BookLine("congregate and the future", 71),
                        BookLine("home of the Assembly of", 72),
                        BookLine("Elders.", 73),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Over the following days, Seren", 55),
                        BookLine("imparted her idea for a new", 56),
                        BookLine("city, a city where all elves", 57),
                        BookLine("would have access to hear her", 58),
                        BookLine("words, a city where all elves", 59),
                        BookLine("would live in peace, a living", 60),
                        BookLine("city made of the very same", 61),
                        BookLine("crystal that she herself was", 62),
                        BookLine("comprised.", 63),
                    ),
                    Page(
                        BookLine("It was decided that the city", 66),
                        BookLine("should be set out in an", 67),
                        BookLine("octagon. Each of the eight", 68),
                        BookLine("clans would have their own", 69),
                        BookLine("segment of the city.", 70),
                        BookLine("Each segment would be edged", 71),
                        BookLine("on one side by a city wall", 72),
                        BookLine("with its opposing corner at", 73),
                        BookLine("the city center, where the", 74),
                        BookLine("Tower of Voices was located.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("This would give each of the", 55),
                        BookLine("clans equal access to hear the", 56),
                        BookLine("words of Seren and equal", 57),
                        BookLine("responsibility for the city's", 58),
                        BookLine("protection in the case of", 59),
                        BookLine("a siege. Seren then provided", 60),
                        BookLine("one special seed for every", 61),
                        BookLine("clan, telling them that each", 62),
                        BookLine("seed should be planted", 63),
                        BookLine("in the center of each", 64),
                    ),
                    Page(
                        BookLine("clan's segment of the", 66),
                        BookLine("city. The eight clans did", 67),
                        BookLine("this, and found that the", 68),
                        BookLine("seeds grew into great towers.", 69),
                        BookLine("Before long, there were nine", 70),
                        BookLine("crystal towers in the clearing.", 71),
                        BookLine("And so, the city of Prifddinas", 72),
                        BookLine("was born.", 73),
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
        on(Items.PRIFDDINAS_HISTORY_6073, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
