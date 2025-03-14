package content.global.handlers.item.book.journal

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class RandalJournal : InteractionListener {
    /*
     * Randas's Journal is a quest item used in the Underground pass quest.
     * Obtaining and reading it is not required to complete the quest.
     * It is found on the ground in a room next to the long dead warrior Randas.
     * The room it is found in has an altar and a well, and is located deep
     * within the Underground Pass.
     */

    companion object {
        private const val TITLE = "Randas's Journal"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("I came to cleanse these", 55),
                        BookLine("mountain passes of the", 56),
                        BookLine("dark forces that dwell", 57),
                        BookLine("here. I knew my journey", 58),
                        BookLine("would be treacherous, so", 59),
                        BookLine("I deposited Spheres of", 60),
                        BookLine("Light in some of", 61),
                        BookLine("the tunnels.", 62),
                        BookLine("These spheres are a beacon", 63),
                        BookLine("of safety for all who come.", 64),
                        BookLine("The spheres were created", 65),
                    ),
                    Page(
                        BookLine("by Saradominist mages.", 66),
                        BookLine("When held they boost our", 67),
                        BookLine("faith and courage. I still", 68),
                        BookLine("feel...", 69),
                        BookLine("Iban relentlessly tugging...", 71),
                        BookLine("at my weak soul......", 73),
                        BookLine("bringing out any innate", 75),
                        BookLine("goodness to ones heart,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("illuminating the dark", 55),
                        BookLine("caverns with the light", 56),
                        BookLine("of Saradomin, bringing", 57),
                        BookLine("fear and pain to all", 58),
                        BookLine("who embrace the dark side.", 59),
                        BookLine("My men are still", 60),
                        BookLine("repelled by 'Ibans will',", 61),
                        BookLine("it seems as if their pure", 62),
                        BookLine("hearts bar them from", 63),
                        BookLine("entering Ibans realm.", 64),
                        BookLine("My turn has come,", 65),
                    ),
                    Page(
                        BookLine("I dare not admit it to", 66),
                        BookLine("my loyal men, but I", 67),
                        BookLine("fear for the welfare", 68),
                        BookLine("of my soul", 69),
                        BookLine("this new world.", 70),
                        BookLine("Not much is known about", 71),
                        BookLine("the first generations", 72),
                        BookLine("of elves to walk this land.", 73),
                        BookLine("The only remaining", 74),
                        BookLine("documentation is the", 75),
                        BookLine("ancestry records.", 76),
                    ),
                    Page(
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
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.OLD_JOURNAL_1493, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
