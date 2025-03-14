package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class EasternDiscoveryBook : InteractionListener {
    /*
     * Explains some of the history after the God Wars.
     */

    companion object {
        private const val TITLE = "Exploration of the Eastern Realm"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Preface:", 55),
                        BookLine("This volume is meant", 56),
                        BookLine("as a historical", 57),
                        BookLine("document covering the", 58),
                        BookLine("period between 'The break", 59),
                        BookLine("out' and the creation", 60),
                        BookLine("of the eastern province.", 61),
                        BookLine("You will find the book", 62),
                        BookLine("is written from a third", 63),
                        BookLine("party point of view;", 64),
                        BookLine("this is in an attempt", 65),
                    ),
                    Page(
                        BookLine("to make this literature", 66),
                        BookLine("more objective.", 67),
                        BookLine("<col=08088A>Chapter 1: The break out.", 69),
                        BookLine("In matters of history", 70),
                        BookLine("it is always best to", 71),
                        BookLine("understand some background", 72),
                        BookLine("to the subject you are", 73),
                        BookLine("studying, so we shall", 74),
                        BookLine("have a brief explanation", 75),
                        BookLine("of 'The break out'.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("After centuries of", 55),
                        BookLine("isolated development,", 56),
                        BookLine("after the god wars, it", 57),
                        BookLine("was decided that the", 58),
                        BookLine("Elven domain needed to", 59),
                        BookLine("expand. With their backs", 60),
                        BookLine("to the sea, there was", 61),
                        BookLine("only one way to go: East.", 62),
                        BookLine("After many years of hard", 63),
                        BookLine("work, a pass over the", 64),
                        BookLine("mountains was created.", 65),
                    ),
                    Page(
                        BookLine("The creation of this", 66),
                        BookLine("pass is a long tale unto", 67),
                        BookLine("itself and we shall leave", 68),
                        BookLine("that subject for another", 69),
                        BookLine("time.", 70),
                        BookLine("<col=08088A>Chapter 2: First impressions.", 72),
                        BookLine("For the first time", 73),
                        BookLine("since Seren had departed,", 74),
                        BookLine("the Elven people could", 75),
                        BookLine("leave Tirannwn.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("They found a land in", 55),
                        BookLine("chaos; many of the races", 56),
                        BookLine("that had been dominant", 57),
                        BookLine("before the end of the", 58),
                        BookLine("god wars were on the", 59),
                        BookLine("edge of extinction or", 60),
                        BookLine("had simply vanished. The", 61),
                        BookLine("Gnome city, which in", 62),
                        BookLine("years past had good trade", 63),
                        BookLine("with the Elves, was now", 64),
                        BookLine("little more than a village.", 65),
                    ),
                    Page(
                        BookLine("As for the Mahjarrat,", 66),
                        BookLine("who had never had good", 67),
                        BookLine("relations with Elves,", 68),
                        BookLine("there was no trace. The", 69),
                        BookLine("only sign anyone had", 70),
                        BookLine("ever been there was the", 71),
                        BookLine("sacred battle grounds.", 72),
                        BookLine("Seeing the vacuum in", 73),
                        BookLine("power the Elves elected", 74),
                        BookLine("to move quickly but first", 75),
                        BookLine("they needed some security.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Chapter 3: Treaty.", 56),
                        BookLine("Knowing how volatile", 57),
                        BookLine("the east lands can be,", 58),
                        BookLine("the Assembly of elders", 59),
                        BookLine("concluded that the safest", 60),
                        BookLine("way to proceed was to", 61),
                        BookLine("draw up non-aggression", 62),
                        BookLine("pacts with: the Gnomes", 63),
                        BookLine("to the north; the Humans", 64),
                        BookLine("to the east, and the", 65),
                    ),
                    Page(
                        BookLine("Ogres to the south. The", 66),
                        BookLine("Gnomes, having taken", 67),
                        BookLine("great losses at the end", 68),
                        BookLine("of the god wars, were", 69),
                        BookLine("more than happy to find", 70),
                        BookLine("a race who did not wish", 71),
                        BookLine("them harm and signed", 72),
                        BookLine("up to the treaty immediately.", 73),
                        BookLine("The Humans, being of many", 74),
                        BookLine("different tribes were", 75),
                        BookLine("a little harder to get", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("any agreement from. After", 55),
                        BookLine("much time and organization", 56),
                        BookLine("however, the tribes concluded", 57),
                        BookLine("that a non-aggression", 58),
                        BookLine("pact with the Elves was", 59),
                        BookLine("in all their best interests.", 60),
                        BookLine("Lastly the Elves approached", 61),
                        BookLine("the Ogres and their kin", 62),
                        BookLine("to the south. Unfortunately,", 63),
                        BookLine("without Bandos' guidance", 64),
                        BookLine("which had held these", 65),
                    ),
                    Page(
                        BookLine("races together for millenia,", 66),
                        BookLine("the nation had fallen", 67),
                        BookLine("into a bloody civil war,", 68),
                        BookLine("making any form of a", 69),
                        BookLine("treaty with each side", 70),
                        BookLine("useless.", 71),
                        BookLine("The Elven people", 73),
                        BookLine("decided that two out", 74),
                        BookLine("of three treaties agreed", 75),
                        BookLine("upon was good enough", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to start the colonisation", 55),
                        BookLine("of the new lands. And", 56),
                        BookLine("so the eastern province", 57),
                        BookLine("was born.", 58),
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
        on(Items.EASTERN_DISCOVERY_6075, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
