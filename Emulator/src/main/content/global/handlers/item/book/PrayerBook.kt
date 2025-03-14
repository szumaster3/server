package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class PrayerBook : InteractionListener {
    /*
     * Reward from the Great Brain Robbery quest. By right-clicking on
     * it while wearing the Holy symbol, the player can use Prayer to
     * heal poison at the cost of prayer points. Sometimes the poison
     * will only be partially healed if you run out of prayer points.
     */

    companion object {
        private const val TITLE = "Prayer of Deliverance from Poisons"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Being the full and", 55),
                        BookLine("proper prayer", 56),
                        BookLine("against morbidities of", 57),
                        BookLine("the flesh caused by venoms", 58),
                        BookLine("and potions of both a", 59),
                        BookLine("natural and mystical origin.", 60),
                        BookLine("Take up your blessed symbol", 61),
                        BookLine("of Saradomin, cast in", 62),
                        BookLine("silver. Hang it about", 63),
                        BookLine("thy neck, where all may", 64),
                    ),
                    Page(
                        BookLine("see it and thus know of", 66),
                        BookLine("your devotion. Pray in", 67),
                        BookLine("a loud, clear voice to", 68),
                        BookLine("Saradomin for relief from", 69),
                        BookLine("that which ails you.", 70),
                        BookLine("Appropriate calls", 71),
                        BookLine("for aid should be", 72),
                        BookLine("accompanied by a sacrifice", 73),
                        BookLine("of Prayer energy, with", 74),
                        BookLine("deadlier venoms requiring", 75),
                        BookLine("a greater sacrifice. Should", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("the supplicant lack piety,", 55),
                        BookLine("they will not be cured.", 56),
                        BookLine("Should they posses a measure", 57),
                        BookLine("of piety equal or even", 58),
                        BookLine("great than that which", 59),
                        BookLine("assails them, the poison", 60),
                        BookLine("will be reduced or even", 61),
                        BookLine("cured, while a like amount", 62),
                        BookLine("of Prayer energy will", 63),
                        BookLine("be taken to do this. Be", 64),
                        BookLine("wary: should the", 65),
                    ),
                    Page(
                        BookLine("supplicant only", 66),
                        BookLine("partially remove", 67),
                        BookLine("the venom through the", 68),
                        BookLine("application of prayer,", 69),
                        BookLine("their body must still", 70),
                        BookLine("bear the brunt of its", 71),
                        BookLine("wickedness. Also...", 72),
                        BookLine("(The rest of the book", 73),
                        BookLine("seems to have been", 74),
                        BookLine("vandalised, charred", 75),
                        BookLine("and slightly bled upon.)", 76),
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
        on(Items.PRAYER_BOOK_10890, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
