package content.global.handlers.item.book.notes

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ExplorersNotes : InteractionListener {
    /*
     * Explorer Notes. The book details his journey beyond Trollheim, where
     * he discovered the God Wars Dungeon. It can be found in the Keldagrim
     * Library, on the second level.
     */

    companion object {
        private const val TITLE = "Explorer's notes"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Beyond Trollheim, by Nestor", 55),
                        BookLine("Peregrine There are many", 56),
                        BookLine("stories and histories among", 57),
                        BookLine("the dwarves about the", 58),
                        BookLine("mountains beyond Trollheim,", 59),
                        BookLine("all of which tell of great", 60),
                        BookLine("secrets and wealth lying", 61),
                        BookLine("buried for aeons.", 62),
                        BookLine("Now I've never been able", 63),
                        BookLine("to resist the lure of the", 64),
                        BookLine("unknown, which s the reason", 65),
                        BookLine("that I've explored the", 66),
                        BookLine("length and breadth of", 67),
                        BookLine("Gielinor, so rumours that", 68),
                        BookLine("an ancient temple had been", 69),
                        BookLine("discovered north of the", 70),
                        BookLine("Death Plateau had me", 71),
                        BookLine("jumping to get out there", 72),
                        BookLine("and see for myself.", 73),
                        BookLine("The journey into the", 74),
                        BookLine("mountains was uneventful,", 75),
                        BookLine("save for the amount of", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("time I had to spend finding", 55),
                        BookLine("a reasonable route into", 56),
                        BookLine("the mountains.", 57),
                        BookLine("My first attempt took me", 58),
                        BookLine("to the west of the Ice", 59),
                        BookLine("Path, journeying through", 60),
                        BookLine("the ice gate, but I was", 61),
                        BookLine("thwarted by a huge snowbank,", 62),
                        BookLine("which I couldn't find any", 63),
                        BookLine("way over. I had no desire", 64),
                        BookLine("to pass any pesky trolls,", 65),
                        BookLine("so once again I took the", 66),
                        BookLine("long route. This time I", 67),
                        BookLine("travelled deep into that", 68),
                        BookLine("blighted landscape that", 69),
                        BookLine("is called the Wilderness,", 70),
                        BookLine("only to be thwarted by", 71),
                        BookLine("a steep cliff. At one point", 72),
                        BookLine("I found some purchase in", 73),
                        BookLine("the rock face, but my attempts", 74),
                        BookLine("were futile and I was forced", 75),
                        BookLine("to abseil down the cliff", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to try a different approach.", 55),
                        BookLine("My third attempt was from", 56),
                        BookLine("the south.", 57),
                        BookLine("I snuck past the trolls", 58),
                        BookLine("under cover of darkness", 59),
                        BookLine("and eventually found a", 60),
                        BookLine("small valley that was blocked", 61),
                        BookLine("by rockfall.", 62),
                        BookLine("Fortunately, my small size", 63),
                        BookLine("allowed me to squeeze between", 64),
                        BookLine("the rocks in the avalanche", 65),
                        BookLine("debris and I found myself", 66),
                        BookLine("beset by starving wolves.", 67),
                        BookLine("I fled from them and found", 68),
                        BookLine("signs of an ancient battle,", 69),
                        BookLine("mostly hidden by the snow.", 70),
                        BookLine("The ruins seemed to indicate", 71),
                        BookLine("that a great palace or", 72),
                        BookLine("temple once stood there,", 73),
                        BookLine("but time and events had", 74),
                        BookLine("taken their toll and there", 75),
                        BookLine("was naught but rubble left.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Exploring the area, I found", 55),
                        BookLine("a massive pit where the", 56),
                        BookLine("ground had collapsed, so", 57),
                        BookLine("I tied off my rope and", 58),
                        BookLine("descended, hand over hand,", 59),
                        BookLine("into the maws of hell.", 60),
                        BookLine("Below the ruins was a massive", 61),
                        BookLine("cave, stretching as far", 62),
                        BookLine("as the eye could see and", 63),
                        BookLine("filled with all manner", 64),
                        BookLine("of demons, goblins and", 65),
                        BookLine("strange birdmen, all locked", 66),
                        BookLine("in mortal combat. As I", 67),
                        BookLine("watched, a small battalion", 68),
                        BookLine("of orks surrounded and", 69),
                        BookLine("slew a demon, but were", 70),
                        BookLine("then themselves besieged", 71),
                        BookLine("by a pack of hellhounds!", 72),
                        BookLine("I am an explorer, not a", 73),
                        BookLine("warrior, so I left the", 74),
                        BookLine("same way that I had come,", 75),
                        BookLine("taking my rope with me", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("so that none of those", 55),
                        BookLine("monstrosities could escape", 56),
                        BookLine("their prison.", 57),
                        BookLine("If there WAS treasure or", 58),
                        BookLine("gold to be found in those", 59),
                        BookLine("ruins the risk was not", 60),
                        BookLine("worth the reward, although", 61),
                        BookLine("some may argue with me.", 62),
                        BookLine("One other incident bears", 63),
                        BookLine("remarking upon; while leaving", 64),
                        BookLine("the mountain peaks I stumbled", 65),
                    ),
                    Page(
                        BookLine("upon a small group of undead", 66),
                        BookLine("trolls, marching northwards.", 67),
                        BookLine("All I can say is that trolls", 68),
                        BookLine("are even stupider dead", 69),
                        BookLine("than they are alive, for", 70),
                        BookLine("they did not see me at", 71),
                        BookLine("all - even though I was", 72),
                        BookLine("hiding behind some rubble,", 73),
                        BookLine("only feet from where they", 74),
                        BookLine("marched. I feel that my", 75),
                        BookLine("next journey should take", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("me in a different direction", 55),
                        BookLine("completely. Word has come", 56),
                        BookLine("to me that the poisonous", 57),
                        BookLine("swamps, far to the southwest,", 58),
                        BookLine("may be traversable, which", 59),
                        BookLine("is something I cannot help", 60),
                        BookLine("but explore.", 61),
                        BookLine("Even now I can feel a", 62),
                        BookLine("tingling wanderlust.", 63),
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
        on(Items.EXPLORERS_NOTES_11677, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
