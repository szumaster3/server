package content.global.handlers.item.book.journal

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class FeatheredJournal : InteractionListener {
    /*
     * Book that players will find as part of the Eagles' Peak quest.
     * The book is the journal of the criminal Arthur Artimus, and
     * documents the rise and fall of his gang.
     */

    companion object {
        private const val TITLE = "Feathered journal"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("We're all set for the raid", 55),
                        BookLine("on the Gnome Stronghold", 56),
                        BookLine("tomorrow. They must have", 57),
                        BookLine("something good in a place", 58),
                        BookLine("that well defended. We'll", 59),
                        BookLine("sneak in over the west", 60),
                        BookLine("river while the gnomes", 61),
                        BookLine("are watching some big 'gnome", 62),
                        BookLine("ball' match, whatever that", 63),
                        BookLine("is. This time Armen won't", 64),
                        BookLine("bring his pet kebbit. That", 65),
                    ),
                    Page(
                        BookLine("thing caused nothing but", 66),
                        BookLine("trouble last time. The", 67),
                        BookLine("start to the so called", 68),
                        BookLine("'gnome ball' match appears", 69),
                        BookLine("to have been delayed; some", 70),
                        BookLine("sort of rubbish about bad", 71),
                        BookLine("light. Looks like we're", 72),
                        BookLine("stuck here waiting for", 73),
                        BookLine("now. I'm going to kill", 74),
                        BookLine("Armen, he assured me his", 75),
                        BookLine("kebbit was tied up safely.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("We were lucky to escape", 55),
                        BookLine("with our lives, especially", 56),
                        BookLine("Davey who got his foot", 57),
                        BookLine("caught in a rabbit hole", 58),
                        BookLine("whilst we were running", 59),
                        BookLine("away. Those long eared", 60),
                        BookLine("pests seem to get everywhere.", 61),
                        BookLine("Currently hiding in a cave", 62),
                        BookLine("in the mountains. The only", 63),
                        BookLine("'loot' we managed to grab", 64),
                        BookLine("was a bag of refreshments", 65),
                    ),
                    Page(
                        BookLine("from the gnome ball pitch.", 66),
                        BookLine("There'd better be something", 67),
                        BookLine("good in there, I could", 68),
                        BookLine("eat a camel right now. Gnomes", 69),
                        BookLine("are still looking for us", 70),
                        BookLine("outside, might be in here", 71),
                        BookLine("for a while. Fortunately", 72),
                        BookLine("this cave system seems", 73),
                        BookLine("to go a lot deeper than", 74),
                        BookLine("we originally thought,", 75),
                        BookLine("although some of the noises", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("coming from the darker", 55),
                        BookLine("areas concern me slightly.", 56),
                        BookLine("Barakur has been playing", 57),
                        BookLine("with mechanisms as per", 58),
                        BookLine("usual. He thinks he can", 59),
                        BookLine("rig up something to hide", 60),
                        BookLine("the cave entrance. Is this", 61),
                        BookLine("what dwarves do when they", 62),
                        BookLine("get bored? Armen said he", 63),
                        BookLine("saw some kind of giant", 64),
                        BookLine("bird. I'm skeptical, although", 65),
                    ),
                    Page(
                        BookLine("this place does give me", 66),
                        BookLine("the creeps. Gnome food", 67),
                        BookLine("again today. Worms, however,", 68),
                        BookLine("they're cooked, get boring", 69),
                        BookLine("after a while. Okay, we", 70),
                        BookLine("all saw the birds this", 71),
                        BookLine("time. Typically Armen insists", 72),
                        BookLine("he can tame them; that", 73),
                        BookLine("guy is animal crazy. Gnomes", 74),
                        BookLine("seem to have stopped looking", 75),
                        BookLine("for us, although with Barakur's", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("hidden entrance this would", 55),
                        BookLine("be a good place to stash", 56),
                        BookLine("loot. Armen tried roping", 57),
                        BookLine("one of the eagles today;", 58),
                        BookLine("got dragged halfway to", 59),
                        BookLine("Al Kharid apparently. Barakur", 60),
                        BookLine("seems to have taken the", 61),
                        BookLine("hideout idea to heart and", 62),
                        BookLine("is now designing an inner", 63),
                        BookLine("door. A mysterious old", 64),
                        BookLine("man turned up today. Not", 65),
                    ),
                    Page(
                        BookLine("sure how he got in. Kept", 66),
                        BookLine("pestering until eventually", 67),
                        BookLine("Davey spoke to him, at", 68),
                        BookLine("which point he offloaded", 69),
                        BookLine("a load of spinach rolls", 70),
                        BookLine("onto us then left. Grief,", 71),
                        BookLine("Barakur's door design is", 72),
                        BookLine("contrived; not sure I'll", 73),
                        BookLine("be able to open it myself.", 74),
                        BookLine("However, Armen still insists", 75),
                        BookLine("we'll be able to use the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("eagles to get in and out. First", 55),
                        BookLine("attempt at using the eagles", 56),
                        BookLine("in a raid today. Gave me", 57),
                        BookLine("the fright of my life.", 58),
                        BookLine("Armen guided the eagle", 59),
                        BookLine("over Ardougne market; managed", 60),
                        BookLine("to catch some loaves with", 61),
                        BookLine("a fishing net. We'll be", 62),
                        BookLine("eating well tonight, at", 63),
                        BookLine("least in comparison to", 64),
                        BookLine("recently. Barakur has completed", 65),
                    ),
                    Page(
                        BookLine("two of apparently three", 66),
                        BookLine("parts of the puzzle for", 67),
                        BookLine("his second door. I'm sure", 68),
                        BookLine("he'd be making quicker", 69),
                        BookLine("progress if Armen's kebbit", 70),
                        BookLine("didn't keep getting in", 71),
                        BookLine("the way. That mysterious", 72),
                        BookLine("old man turned up again.", 73),
                        BookLine("We don't have time for", 74),
                        BookLine("this, quite frankly, so", 75),
                        BookLine("we're just going to ignore", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("him, after all what could", 55),
                        BookLine("he possible...", 56),
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
        on(Items.FEATHERED_JOURNAL_10179, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
