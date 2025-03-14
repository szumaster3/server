package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class TheGreatDivideBook : InteractionListener {
    /*
     * The great divide, written by Cerridwyn Cadarn, is a book which goes on
     * to tell of a time when the Underground Pass was found to be blocked off
     * and, after sending scouts to try to discern what was going on, the elves
     * of the eastern regions found that during their absence, Prifddinas had
     * fallen into a gruesome, bloody civil war, with Lord Iorwerth seizing
     * control of much of the city. The book ends with the elves travelling to
     * return to the city. The book can be found on the shelves in your house after
     * the completion of Mourner's End Part 1.
     */

    companion object {
        private const val TITLE = "The great divide"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("I should start", 55),
                        BookLine("by telling you", 56),
                        BookLine("about some of the events", 57),
                        BookLine("that led up to 'The great", 58),
                        BookLine("divide'. For almost two", 59),
                        BookLine("millenia we lived in", 60),
                        BookLine("the eastern realm, then", 61),
                        BookLine("in the year 1930 we lost", 62),
                        BookLine("all contact with our", 63),
                        BookLine("people on the other side", 64),
                        BookLine("of Arandar. We found", 65),
                    ),
                    Page(
                        BookLine("that the route over the", 66),
                        BookLine("mountains was impassable", 67),
                        BookLine("and our incantations", 68),
                        BookLine("were unable to penetrate", 69),
                        BookLine("a wall of static that", 70),
                        BookLine("now surrounded our homeland.", 71),
                        BookLine("An emergency council was", 72),
                        BookLine("called, which I was lucky", 73),
                        BookLine("enough to take part in.", 74),
                        BookLine("We decided that before", 75),
                        BookLine("any major action could", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("be taken, we needed to", 55),
                        BookLine("know what had happened.", 56),
                        BookLine("Many ways to re-enter", 57),
                        BookLine("Tirannwn were discussed.", 58),
                        BookLine("It was then that I reminded", 59),
                        BookLine("the council of an old", 60),
                        BookLine("cave system that linked", 61),
                        BookLine("up to the 'Well of Voyage'.", 62),
                        BookLine("An ancient device made", 63),
                        BookLine("by our forefathers to", 64),
                        BookLine("give us access to the", 65),
                    ),
                    Page(
                        BookLine("deep caves, though why", 66),
                        BookLine("anyone would go to that", 67),
                        BookLine("god forsaken place I", 68),
                        BookLine("have no idea. Soon it", 69),
                        BookLine("was decided to send five", 70),
                        BookLine("members of the Cadarn", 71),
                        BookLine("clan through the 'Well", 72),
                        BookLine("of Voyage', to recover", 73),
                        BookLine("any information that", 74),
                        BookLine("could tell us what was", 75),
                        BookLine("happening. We chose to", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("re-convene upon their", 55),
                        BookLine("return. After a few weeks", 56),
                        BookLine("Adwr returned, the only", 57),
                        BookLine("one of the five scouts", 58),
                        BookLine("that made it back. A", 59),
                        BookLine("council was called to", 60),
                        BookLine("listen to his news. He", 61),
                        BookLine("told us of the chaos", 62),
                        BookLine("he had seen; elves killing", 63),
                        BookLine("Elves in the streets", 64),
                        BookLine("of Prifddinas and the", 65),
                    ),
                    Page(
                        BookLine("seizing of the 'Tower", 66),
                        BookLine("of Voices' by clan Iorwerth,", 67),
                        BookLine("their forces denying", 68),
                        BookLine("access to those not of", 69),
                        BookLine("their bloodline. Hearing", 70),
                        BookLine("all this sparked a debate", 71),
                        BookLine("that continued for days.", 72),
                        BookLine("Many plans were put forward", 73),
                        BookLine("of how we should deal", 74),
                        BookLine("with the problems back", 75),
                        BookLine("home. Ideas ranging from", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("the assassination of", 55),
                        BookLine("lord Iorwerth to just", 56),
                        BookLine("leaving the western Elves", 57),
                        BookLine("to kill each other were", 58),
                        BookLine("suggested. It was at", 59),
                        BookLine("this suggestion that", 60),
                        BookLine("the Baxtorian quoted", 61),
                        BookLine("the 'Cerddi', telling", 62),
                        BookLine("all those present that", 63),
                        BookLine("'Only together as the", 64),
                        BookLine("eight clans can we hope", 65),
                    ),
                    Page(
                        BookLine("to survive in this hostile", 66),
                        BookLine("new world', he then pledged", 67),
                        BookLine("clan Cadarn to stop the", 68),
                        BookLine("madness that had befallen", 69),
                        BookLine("Prifddinas and so started", 70),
                        BookLine("the Baxtorian campaign.", 71),
                        BookLine("After three days of", 72),
                        BookLine("preparation, the Cadarn", 73),
                        BookLine("clan was ready to", 74),
                        BookLine("move. I also chose", 75),
                        BookLine("to join the Cadarn forces", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to keep records of what", 55),
                        BookLine("transpired. So it was", 56),
                        BookLine("that we made our way", 57),
                        BookLine("to the entrance of the", 58),
                        BookLine("cave system that eventually", 59),
                        BookLine("led to the 'Well of Voyage'.", 60),
                        BookLine("Travelling in such places", 61),
                        BookLine("is never exactly easy,", 62),
                        BookLine("and this was no exception.", 63),
                        BookLine("To transport a whole", 64),
                        BookLine("army and our supplies", 65),
                    ),
                    Page(
                        BookLine("through the cave system", 66),
                        BookLine("took us close to a week.", 67),
                        BookLine("It was not until...", 68),
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
        on(Items.THE_GREAT_DIVIDE_6079, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
