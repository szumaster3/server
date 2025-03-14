package content.global.handlers.item.book.guide

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class BarbarianAssaultGuide : InteractionListener {
    /*
     * Obtainable during the Barbarian Assault minigame.
     */

    companion object {
        private const val TITLE = "Queen help book"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>1) Collector:</col>", 55),
                        BookLine("Pick up yellow", 56),
                        BookLine("egg. Pass to healer. Take", 57),
                        BookLine("omega egg from Defender.", 58),
                        BookLine("Load into turret.", 59),
                        BookLine("<col=08088A>2) Healer:</col>", 61),
                        BookLine("Take yellow egg from", 62),
                        BookLine("Collector. Poison egg", 63),
                        BookLine("in pool. Pass to", 64),
                        BookLine("attacker.", 65),
                    ),
                    Page(
                        BookLine("<col=08088A>3) Attacker:</col>", 66),
                        BookLine("Take poisoned, yellow", 67),
                        BookLine("egg from healer.", 68),
                        BookLine("Add spikes from mushroom.", 69),
                        BookLine("Pass to defender.", 70),
                        BookLine("<col=08088A>4) Defender:</col>", 72),
                        BookLine("Take poisoned, spiked,", 73),
                        BookLine("yellow egg from Attacker.", 74),
                        BookLine("Dunk in lava. Pass", 75),
                        BookLine("to Collector.", 76),
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
        on(Items.QUEEN_HELP_BOOK_10562, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
