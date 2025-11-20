package content.global.plugins.item.books_and_scrolls.guide

import content.global.plugins.iface.BookInterface
import content.global.plugins.iface.BookLine
import content.global.plugins.iface.Page
import content.global.plugins.iface.PageSet
import core.api.sendString
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.DARK_BLUE
import core.tools.DARK_RED
import shared.consts.Items

class BarbarianAssaultGuide : InteractionListener {

    /*
     * Obtained during the Barbarian Assault minigame
     * after "The Final Battle" tutorial with Captain Cain.
     * Authentic state.
     */

    companion object {
        private const val TITLE = "Queen Help"
        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("${DARK_RED}1)$DARK_BLUE Collector:</col> Pick up",    55),
                    BookLine("yellow egg. Pass to",                                 56),
                    BookLine("healer.",                                             57),
                    BookLine("Take omega egg from",                                 58),
                    BookLine("Defender. Load into",                                 59),
                    BookLine("turret.",                                             60),
                    BookLine("",                                                    61),
                    BookLine("${DARK_RED}2)$DARK_BLUE Healer:</col> Take yellow",   62),
                    BookLine("egg from Collector.",                                 63),
                    BookLine("Poison egg in pool. Pass",                            64),
                    BookLine("to attacker.",                                        65),
                ),
                Page(
                    BookLine("${DARK_RED}3)$DARK_BLUE Attacker:</col> Take",        66),
                    BookLine("poisoned, yellow egg from",                           67),
                    BookLine("healer. Add spikes from",                             68),
                    BookLine("mushroom. Pass to",                                   69),
                    BookLine("defender.",                                           70),
                    BookLine("",                                                    71),
                    BookLine("${DARK_RED}4)$DARK_BLUE Defender:</col> Take",        72),
                    BookLine("poisoned, spiked, yellow",                            73),
                    BookLine("egg from Attacker. Dunk",                             74),
                    BookLine("in lava. Pass to Collector.",                         75),
                ),
            ),
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        // Hides page numbers.
        sendString(player, "", BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_LINE_IDS[1])
        sendString(player, "", BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_LINE_IDS[2])
        return true
    }

    override fun defineListeners() {
        on(Items.QUEEN_HELP_BOOK_10562, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
