package content.global.handlers.item.book.tome

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class DemonicTome : InteractionListener {
    /*
     * Obtainable during the Shadow of the Storm quest.
     */

    companion object {
        private const val TITLE = "The Confession of Ellemar"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("I know that it is too late", 55),
                        BookLine("to prevent my execution,", 56),
                        BookLine("and I do not dispute the", 57),
                        BookLine("letter of the charges", 58),
                        BookLine("against me. I did read", 59),
                        BookLine("forbidden books, and I", 60),
                        BookLine("did perform unholy", 61),
                        BookLine("rituals forbidden by the", 62),
                        BookLine("Church of Saradomin.", 63),
                        BookLine("But I hope to show by", 64),
                        BookLine("this account that I did", 65),
                    ),
                    Page(
                        BookLine("not blaspheme against", 66),
                        BookLine("holy Saradomin but", 67),
                        BookLine("rather acted in his spirit.", 68),
                        BookLine("", 69),
                        BookLine("For as long as we know,", 70),
                        BookLine("those who worship", 71),
                        BookLine("Saradomin have fallen", 72),
                        BookLine("prey to demons, both", 73),
                        BookLine("those that are summoned", 74),
                        BookLine("by dark wizards and", 75),
                        BookLine("those that live wild in the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("way that unholy magic", 55),
                        BookLine("could be used for a holy", 56),
                        BookLine("purpose.", 57),
                        BookLine("", 58),
                        BookLine("Not all demons are most", 59),
                        BookLine("dangerous when they are", 60),
                        BookLine("on our plane as physical", 61),
                        BookLine("being. There is also a", 62),
                        BookLine("more subtle demonic", 63),
                        BookLine("influence that prevades", 64),
                        BookLine("our world, with demons", 65),
                    ),
                    Page(
                        BookLine("working behind the scenes", 66),
                        BookLine("to cause natural disaters", 67),
                        BookLine("and accidents. the chief", 68),
                        BookLine("among these demons is", 69),
                        BookLine("Agrith-Naar.", 70),
                        BookLine("", 71),
                        BookLine("If Agrith-Naar were to", 72),
                        BookLine("be removed from his own", 73),
                        BookLine("dimension he would be", 74),
                        BookLine("unable to work his magic", 75),
                        BookLine("on the world, and so I", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("came to beleive that", 55),
                        BookLine("summoning him would", 56),
                        BookLine("not be an evil act, but a", 57),
                        BookLine("good one. Therefore I", 58),
                        BookLine("secretly studied the", 59),
                        BookLine("forbidden books and", 60),
                        BookLine("conducted magical", 61),
                        BookLine("experiments, until I had", 62),
                        BookLine("discovered the means by", 63),
                        BookLine("which Agrith-Naar could", 64),
                        BookLine("be summoned. The", 65),
                    ),
                    Page(
                        BookLine("process of preparation", 66),
                        BookLine("was complex, but the most", 67),
                        BookLine("important part was the", 68),
                        BookLine("construction of a sigil of", 69),
                        BookLine("the demon out of silver.", 70),
                        BookLine("The final act was for", 71),
                        BookLine("eight persons, each", 72),
                        BookLine("holding such a sigil, to", 73),
                        BookLine("recite the following", 74),
                        BookLine("incantation:", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Terren Camerinthum", 55),
                        BookLine("Nahudu Agrith-Naar", 56),
                        BookLine("Caldar", 57),
                        BookLine("", 58),
                        BookLine("The ritual was a success,", 59),
                        BookLine("and the great demon", 60),
                        BookLine("Agrith-Naar appeared", 61),
                        BookLine("before us. We had", 62),
                        BookLine("prepared a magical cage,", 63),
                        BookLine("and with dificulty we", 64),
                        BookLine("imprisoned the demon.", 65),
                    ),
                    Page(
                        BookLine("However, we were unable", 66),
                        BookLine("to destroy him, though we", 67),
                        BookLine("tried every physical and", 68),
                        BookLine("amgical means.", 69),
                        BookLine("Furthermore, the magical", 70),
                        BookLine("cage was weakoning, and", 71),
                        BookLine("we feared it would not be", 72),
                        BookLine("able to contain him for", 73),
                        BookLine("long. Therefore we", 74),
                        BookLine("decided to", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("(The last pages of the", 55),
                        BookLine("book have been torn out)", 56),
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
        on(Items.DEMONIC_TOME_6749, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
