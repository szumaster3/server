package content.region.fremennik.handlers.lunar

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class MoonclanManual : InteractionListener {
    companion object {
        private val TITLE = "The Basics of Magic"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("The Basics of Magic", 55),
                        BookLine("A Primer in the Mystical", 57),
                        BookLine("Arts", 58),
                    ),
                    Page(
                        BookLine("This primer has been", 66),
                        BookLine("collated from years of", 67),
                        BookLine("our research into the", 68),
                        BookLine("metanatural arts", 69),
                        BookLine("commonly known as", 70),
                        BookLine("'magic'.", 71),
                        BookLine("All those wishing to set", 72),
                        BookLine("forth upon the Astral", 73),
                        BookLine("Path should read this", 74),
                        BookLine("primer until the concepts", 75),
                        BookLine("within are fully", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("understood so that their", 55),
                        BookLine("potential may be fully", 56),
                        BookLine("realised.", 57),
                    ),
                    Page(
                        BookLine("Chapter 1", 66),
                        BookLine("Myths & Misconceptions", 68),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Those familiar with the", 55),
                        BookLine("history of our people will", 56),
                        BookLine("know that the source of", 57),
                        BookLine("'magic' has always been a", 58),
                        BookLine("contentious issue.", 59),
                        BookLine("", 60),
                        BookLine("Our Fremennik", 61),
                        BookLine("forefathers believe 'magic'", 62),
                        BookLine("to be a possession of the", 63),
                        BookLine("gods, and that our use of", 64),
                        BookLine("it risks far more than", 65),
                    ),
                    Page(
                        BookLine("any benefits it may", 66),
                        BookLine("bring;", 67),
                        BookLine("We of course take the", 68),
                        BookLine("more enlightened view", 69),
                        BookLine("that we should use all", 70),
                        BookLine("tools at our disposal for", 71),
                        BookLine("our own betterment.", 72),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Beyond any ethical or", 55),
                        BookLine("moral issues surrounding", 56),
                        BookLine("magic however, lies the", 57),
                        BookLine("key question:", 58),
                        BookLine("What exactly is magic?", 59),
                        BookLine("The other obvious", 60),
                        BookLine("question that presents", 61),
                        BookLine("itself then would be:", 62),
                        BookLine("Where exactly does", 63),
                        BookLine("it come from?", 64),
                    ),
                    Page(
                        BookLine("I intend to explain here", 66),
                        BookLine("how both of these", 67),
                        BookLine("questions are actually the", 68),
                        BookLine("same question phrased", 69),
                        BookLine("differently.", 70),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The first major", 55),
                        BookLine("misconception relating to", 56),
                        BookLine("magic is that the stones", 57),
                        BookLine("we use for our arts", 58),
                        BookLine("somehow contain magic", 59),
                        BookLine("within them.", 60),
                        BookLine("", 61),
                        BookLine("Although roughly correct,", 62),
                        BookLine("to believe this is how", 63),
                        BookLine("magic works limits your", 64),
                        BookLine("understanding of our", 65),
                    ),
                    Page(
                        BookLine("own potential, and will", 66),
                        BookLine("prevent you ever", 67),
                        BookLine("achieving the feats which", 68),
                        BookLine("we are all capable of.", 69),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Rather, the 'rune stones'", 55),
                        BookLine("that we shape to our", 56),
                        BookLine("purposes serve to focus", 57),
                        BookLine("your own power, rather", 58),
                        BookLine("than containing the power", 59),
                        BookLine("themselves.", 60),
                    ),
                    Page(
                        BookLine("A short history of how the", 66),
                        BookLine("runes were first", 67),
                        BookLine("discovered will serve to", 68),
                        BookLine("illuminate this point to", 69),
                        BookLine("you more fully.", 70),
                        BookLine("", 71),
                        BookLine("As we all know, from the", 72),
                        BookLine("tales told to us in the", 73),
                        BookLine("Secret Tongue, 'magic'", 74),
                        BookLine("was first discovered in", 75),
                        BookLine("these lands by V-------", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("when he accidentally", 55),
                        BookLine("stumbled upon the Stone", 56),
                        BookLine("of J--.", 57),
                        BookLine("The stone was clearly not", 58),
                        BookLine("of this world, and the", 59),
                        BookLine("mere touch of it unlocked", 60),
                        BookLine("something within the mind", 61),
                        BookLine("of V-------.", 62),
                        BookLine("As we know, the stone", 63),
                        BookLine("was instantly removed by", 64),
                        BookLine("those powers who walk a", 65),
                    ),
                    Page(
                        BookLine("higher Astral Path than", 66),
                        BookLine("ourselves, but its very", 67),
                        BookLine("existence showed V-------", 68),
                        BookLine("possibilities that had never", 69),
                        BookLine("occurred to our kind", 70),
                        BookLine("before.", 71),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("His lifelong search for the", 55),
                        BookLine("stone never did reveal its", 56),
                        BookLine("eventual fate, and the", 57),
                        BookLine("myths suggest that the", 58),
                        BookLine("stone may yet still lie", 59),
                        BookLine("somewhere within this", 60),
                        BookLine("world, but he did discover", 61),
                        BookLine("something of great", 62),
                        BookLine("importance in the caves", 63),
                        BookLine("where first he found the", 64),
                        BookLine("stone; the very rocks that", 65),
                    ),
                    Page(
                        BookLine("had surrounded it had", 66),
                        BookLine("been subtly changed by", 67),
                        BookLine("its presence.", 68),
                        BookLine("The rocks in their", 69),
                        BookLine("contact with the Stone of", 70),
                        BookLine("J-- had gained a", 71),
                        BookLine("yearning to be something ", 72),
                        BookLine("they were not, and", 73),
                        BookLine("V------- discovered that by", 74),
                        BookLine("focussing thought upon", 75),
                        BookLine("them he could transform", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("them entirely for short", 55),
                        BookLine("periods of time into the", 56),
                        BookLine("very elements of the", 57),
                        BookLine("universe.", 58),
                    ),
                    Page(
                        BookLine("This is a mere parlour", 66),
                        BookLine("trick, as many of our", 67),
                        BookLine("young have done the", 68),
                        BookLine("same thing with essence", 69),
                        BookLine("as children, making the", 70),
                        BookLine("cold-burning fire in their", 71),
                        BookLine("hands on summer nights,", 72),
                        BookLine("and V------- knew that", 73),
                        BookLine("should be able to affect", 74),
                        BookLine("a permanent change upon", 75),
                        BookLine("these stones, he could", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("have access to the very", 55),
                        BookLine("powers of the gods", 56),
                        BookLine("themselves!", 57),
                    ),
                    Page(
                        BookLine("And so was created the", 66),
                        BookLine("Astral Temple.", 67),
                        BookLine("This was the first, and to", 68),
                        BookLine("this day we honour those", 69),
                        BookLine("who walked before us on", 70),
                        BookLine("the Astral Path, and", 71),
                        BookLine("prevent it falling into the", 72),
                        BookLine("ruin that this dimension", 73),
                        BookLine("has caused to its", 74),
                        BookLine("counterparts.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("By regular concentrated", 55),
                        BookLine("effort of will upon a large", 56),
                        BookLine("dolmen of essence, by a", 57),
                        BookLine("number of people", 58),
                        BookLine("simultaneously, the", 59),
                        BookLine("dolmen was finally", 60),
                        BookLine("convinced that it had", 61),
                        BookLine("become something which it", 62),
                        BookLine("was not. The Astral", 63),
                        BookLine("Rune, symbol of our", 64),
                        BookLine("very way of life, it is a", 65),
                    ),
                    Page(
                        BookLine("testament to what is", 66),
                        BookLine("possible if we seek", 67),
                        BookLine("understanding.", 68),
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
        on(Items.MOONCLAN_MANUAL_9078, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
