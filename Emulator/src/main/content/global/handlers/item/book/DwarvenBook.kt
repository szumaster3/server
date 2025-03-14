package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class DwarvenBook : InteractionListener {
    /*
     * Obtainable from Rolad during the Between a Rock... quest.
     */

    companion object {
        private const val TITLE = "The Arzinian Being of Bordanzan"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("...in such a way that the", 55),
                        BookLine("miners could no longer", 56),
                        BookLine("safely go down into the", 57),
                        BookLine("mines that were so important", 58),
                        BookLine("to them. Thus the Elders,", 59),
                        BookLine("in their great wisdom,", 60),
                        BookLine("decided to harness the", 61),
                        BookLine("great power of the yellow", 62),
                        BookLine("stones and drive the spirit", 63),
                        BookLine("out of the area. They knew", 64),
                        BookLine("that without the power", 65),
                    ),
                    Page(
                        BookLine("of the stones, they would", 66),
                        BookLine("face a foe too powerful", 67),
                        BookLine("to beat. So for three days", 68),
                        BookLine("and three nights the Arzinian", 69),
                        BookLine("was chased through the", 70),
                        BookLine("twisty passages, always", 71),
                        BookLine("eluding its hunters, until", 72),
                        BookLine("finally it was cornered", 73),
                        BookLine("inside a small tunnel.", 74),
                        BookLine("On either side the brethren", 75),
                        BookLine("approached, their hands", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("sparkling with yellow light,", 55),
                        BookLine("so attempting to banish", 56),
                        BookLine("the spirit back to Bordanzan", 57),
                        BookLine("from whence it came. But", 58),
                        BookLine("lo! The roof did shake", 59),
                        BookLine("and the walls did crumble", 60),
                        BookLine("and the tunnel did collapse.", 61),
                        BookLine("And all who were standing", 62),
                        BookLine("outside rushed inward to", 63),
                        BookLine("aid their fallen brothers,", 64),
                        BookLine("their yellow stones in", 65),
                    ),
                    Page(
                        BookLine("hand. Their combined power", 66),
                        BookLine("proved too much for the", 67),
                        BookLine("Arzinian Being and so it", 68),
                        BookLine("collapsed into itself as", 69),
                        BookLine("the tunnel had collapsed", 70),
                        BookLine("onto its hunters. But instead", 71),
                        BookLine("of being driven out, the", 72),
                        BookLine("monstrous creature trapped", 73),
                        BookLine("into its Bordanzian essense,", 74),
                        BookLine("so that all stone and debris", 75),
                        BookLine("coalesced into a single", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("rock. Thus, trapped but", 55),
                        BookLine("not banished to the nightmare", 56),
                        BookLine("of the Bordanzan, set inside", 57),
                        BookLine("a rock filled with the", 58),
                        BookLine("yellow power stones, the", 59),
                        BookLine("Arzinian...", 60),
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
        on(Items.DWARVEN_LORE_4568, IntType.ITEM, "read") { player, _ ->
            setAttribute(player, "bookInterfaceCallback", ::display)
            setAttribute(player, "bookInterfaceCurrentPage", 0)
            display(player, 0, 0)
            return@on true
        }
    }
}
