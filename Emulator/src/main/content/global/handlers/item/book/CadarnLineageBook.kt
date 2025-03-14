package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class CadarnLineageBook : InteractionListener {
    /*
     * Obtainable from Golrie is a gnome found in the jail
     * of the Gnome Village Dungeon While doing Roving elves,
     * but I not found source when.
     */

    companion object {
        private const val TITLE = "Cadarn Lineage"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("The Cadarn family is one", 55),
                        BookLine("of the oldest elf clan", 56),
                        BookLine("names. Able to trace its", 57),
                        BookLine("blood line back to", 58),
                        BookLine("one of the eight families", 59),
                        BookLine("brought here by Seren in", 60),
                        BookLine("the First Age, it was the", 61),
                        BookLine("Cadarn's responsibility", 62),
                        BookLine("to protect the other seven", 63),
                        BookLine("clans from any hostility", 64),
                        BookLine("encountered while colonising", 65),
                    ),
                    Page(
                        BookLine("this new world.", 66),
                        BookLine("Not much is known about", 67),
                        BookLine("the first generations", 68),
                        BookLine("of elves to walk this land.", 69),
                        BookLine("The only remaining", 70),
                        BookLine("documentation is the", 71),
                        BookLine("ancestry records.", 72),
                        BookLine("It's not until the start", 73),
                        BookLine("of the Third Age that we", 74),
                        BookLine("find surviving transcripts,", 75),
                        BookLine("and these are few and far", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("between. In the Third", 55),
                        BookLine("Age there was constant", 56),
                        BookLine("battling between the", 57),
                        BookLine("different gods and their", 58),
                        BookLine("followers.", 59),
                        BookLine("The need for protection", 60),
                        BookLine("in these troubled times", 61),
                        BookLine("made the Cadarn clan one", 62),
                        BookLine("of the most powerful in", 63),
                        BookLine("the elven domain. After", 64),
                        BookLine("centuries of fighting,", 65),
                    ),
                    Page(
                        BookLine("the war finally ended when", 66),
                        BookLine("the gods realised that", 67),
                        BookLine("they would destroy the", 68),
                        BookLine("very thing they were", 69),
                        BookLine("fighting over.", 70),
                        BookLine("The gods chose to", 71),
                        BookLine("agree on a pact of", 72),
                        BookLine("non-influence.", 73),
                        BookLine("With no great threat left,", 74),
                        BookLine("the Cadarn clan found that", 75),
                        BookLine("their skills were of little", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("use in Tirannwn. At the", 55),
                        BookLine("start of the Fourth Age", 56),
                        BookLine("the Cadarn family spearheaded", 57),
                        BookLine("an expansion into the lands", 58),
                        BookLine("east of Arandar. The occupation", 59),
                        BookLine("lasted for several centuries,", 60),
                        BookLine("before its final collapse", 61),
                        BookLine("near the end of the Fourth", 62),
                        BookLine("Age. With the death of", 63),
                        BookLine("Baxtorian Cadarn, and his", 64),
                        BookLine("sons not yet of age to", 65),
                    ),
                    Page(
                        BookLine("rule, the elven clan returned", 66),
                        BookLine("to Tirannwn leaderless.", 67),
                        BookLine("The Cadarn clan lost much", 68),
                        BookLine("standing within the elven", 69),
                        BookLine("council in this time. This", 70),
                        BookLine("led to a cessation of all", 71),
                        BookLine("exploits within the land", 72),
                        BookLine("to the east, and the eventual", 73),
                        BookLine("closure of all routes through", 74),
                        BookLine("Arandar.", 75),
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
        on(Items.CADARN_LINEAGE_4209, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
