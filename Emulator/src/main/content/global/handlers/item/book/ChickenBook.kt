package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ChickenBook : InteractionListener {
    /*
     * Book about the Evil Chicken found on the bookshelves
     * in the Wise Old Man's house in Draynor Village. The
     * information contained therein probably points to chicken
     * lair in Zanaris.
     */

    companion object {
        private const val TITLE = "The Origins of the Bird of Evil"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Many are the rumours", 55),
                        BookLine("and tales surrounding", 56),
                        BookLine("this foul beast.", 57),
                        BookLine("", 58),
                        BookLine("The earliest of these tales", 59),
                        BookLine("seems to date back to the", 60),
                        BookLine("time of the Mahjarrat. It", 61),
                        BookLine("is a story about a mad", 62),
                        BookLine("mage, who attempts to", 63),
                        BookLine("summon a demon and", 64),
                        BookLine("bind it to his will.", 65),
                    ),
                    Page(
                        BookLine("Unfortunately his spell", 66),
                        BookLine("failed and all that", 67),
                        BookLine("appeared was one", 68),
                        BookLine("confused chicken. In a fit", 69),
                        BookLine("of anger at his failure the", 70),
                        BookLine("mage banished the chicken", 71),
                        BookLine("to the Abyss. The chicken", 72),
                        BookLine("however appears to have", 73),
                        BookLine("survived and grown in", 74),
                        BookLine("power. Years later, when", 75),
                        BookLine("the mage cast another", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("spell of summoning, the", 55),
                        BookLine("chicken appeared! The", 56),
                        BookLine("story does not tell of", 57),
                        BookLine(" what became of the mage.", 58),
                        BookLine("", 59),
                        BookLine("Another popular tale tells", 60),
                        BookLine("of a chicken of higher", 61),
                        BookLine("than average mental", 62),
                        BookLine("ability. Realising that it", 63),
                        BookLine("and its kind were", 64),
                        BookLine("prisoners in their coops", 65),
                    ),
                    Page(
                        BookLine("he organised a rebellion", 66),
                        BookLine("against the human", 67),
                        BookLine("farmers. The farmers, of", 68),
                        BookLine("course, simply slaughtered", 69),
                        BookLine("the rebellious chickens.", 70),
                        BookLine("However, the Evil Chicken", 71),
                        BookLine("escaped. Blaming its", 72),
                        BookLine("brethren for the coup's", 73),
                        BookLine("failure it swore revenge", 74),
                        BookLine("on all chickens and all", 75),
                        BookLine("humans.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("The Chicken can strike", 56),
                        BookLine("anybody, anywhere. It is", 57),
                        BookLine("said that a coven worships", 58),
                        BookLine("this fell fowl and have", 59),
                        BookLine("even built a shrine to it.", 60),
                        BookLine("The exact location of this", 61),
                        BookLine("shrine is unknown, but is", 62),
                        BookLine("rumoured to be in the", 63),
                        BookLine("fairyworlds. Further", 64),
                        BookLine("rumour suggests that his", 65),
                    ),
                    Page(
                        BookLine("lair is guarded by some", 66),
                        BookLine("fearsome beasts, but it", 67),
                        BookLine("may be reached by", 68),
                        BookLine("making a tasty offering...", 69),
                        BookLine("whatever that may be.", 70),
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
        on(Items.BOOK_ON_CHICKENS_7464, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
