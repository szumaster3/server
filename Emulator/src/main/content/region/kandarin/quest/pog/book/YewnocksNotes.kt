package content.region.kandarin.quest.pog.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class YewnocksNotes : InteractionListener {
    companion object {
        private val TITLE = "Yewnock's notes"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("While creating the new anti-", 97),
                        BookLine("illusion device for the Tree", 68),
                        BookLine("Gnome Village from father's", 69),
                        BookLine("notes, I found some", 70),
                        BookLine("interesting snippets about", 71),
                        BookLine("elven magic. The lore that", 72),
                        BookLine("my father has gleaned from", 73),
                        BookLine("the elves if fascinating, and a", 74),
                        BookLine("tribute to his charm and", 75),
                        BookLine("patience—it is not easy to", 76),
                        BookLine("persuade creatures as long-lived", 77),
                        BookLine("as elves to get to the point!", 78),
                        BookLine("Father has shown that small", 79),
                        BookLine("crystal seeds may be grown", 80),
                        BookLine("into more than one", 81),
                    ),
                    Page(
                        BookLine("enchanted artefact.", 82),
                        BookLine("This is a very different", 83),
                        BookLine("means of crafting magical", 84),
                        BookLine("items than is known to", 85),
                        BookLine("most gnomes. It seems to", 86),
                        BookLine("be that, using a static", 87),
                        BookLine("object such as the chanting", 88),
                        BookLine("mechanism (like the singing", 89),
                        BookLine("bowls), can only produce", 90),
                        BookLine("one type of item - the", 91),
                        BookLine("pitch of the bowl's ring", 92),
                        BookLine("defines the nature of the", 93),
                        BookLine("crystal object it forms.", 94),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Since some elves can use", 97),
                        BookLine("their own voices to mould", 68),
                        BookLine("crystal, they can modulate", 69),
                        BookLine("their chanting to create", 70),
                        BookLine("many different items.", 71),
                        BookLine("Father has heard these,", 72),
                        BookLine("and describes them as the", 73),
                        BookLine("most beautiful songs he", 74),
                        BookLine("has ever heard- it must be", 75),
                        BookLine("amazing to hear a true", 76),
                        BookLine("chanter sing.", 77),
                        BookLine("", 78),
                        BookLine("I suspect this is an ability", 79),
                        BookLine("the elves keep to themselves.", 80),
                        BookLine("Us mortals must rely on", 81),
                    ),
                    Page(
                        BookLine("these static crystal", -82),
                        BookLine("shaping devices to sing for us.", 83),
                        BookLine("", 84),
                        BookLine("It also seems clear that", 85),
                        BookLine("if you 'overcharge' a crystal", 86),
                        BookLine("item by producing the same", 87),
                        BookLine("chant, except much louder", 88),
                        BookLine("than normal, you can revert", 89),
                        BookLine("the crystal to seed. This", 90),
                        BookLine("will happen anyway if the", 91),
                        BookLine("magic is drained away.", 92),
                        BookLine("My father also seemed to", 93),
                        BookLine("think that different-sized", 94),
                        BookLine("seeds were used by elves", 95),
                        BookLine("to do different things.", 96),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The larger ones were used", 97),
                        BookLine("for weapons, and the smaller", 68),
                        BookLine("ones for tools. Father", 69),
                        BookLine("had one of the small ones", 70),
                        BookLine("that he had turned into", 71),
                        BookLine("a crystal saw; very useful.", 72),
                        BookLine("He seemed to think the", 73),
                        BookLine("same seed could be turned", 74),
                        BookLine("into other similarly-sized", 75),
                        BookLine("devices with the right", 76),
                        BookLine("chant. One other point", 77),
                        BookLine("he notes - since the singing", 78),
                        BookLine("bowl is also elf crystal ", -79),
                        BookLine("if you knew how to create", 80),
                        BookLine("the right song, you could", 81),
                    ),
                    Page(
                        BookLine("use a singing bowl to make", 82),
                        BookLine("another singing bowl. One", 83),
                        BookLine("could also craft some other", 84),
                        BookLine("type of artefact or device", 85),
                        BookLine("that could itself chant,", 86),
                        BookLine("a musical instrument for", 87),
                        BookLine("example, though what other", 88),
                        BookLine("uses the chant could have", 89),
                        BookLine("apart from moulding crystal", 90),
                        BookLine("was still unknown to my", 91),
                        BookLine("father, though I believe", 92),
                        BookLine("he may have done some", 93),
                        BookLine("experimentation on the", 94),
                        BookLine("matter.", 95),
                    ),
                ),
            )

        @Suppress("UNUSED_PARAMETER")
        private fun display(
            player: Player,
            pageNum: Int,
            buttonID: Int,
        ): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.YEWNOCKS_NOTES_11750, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}
