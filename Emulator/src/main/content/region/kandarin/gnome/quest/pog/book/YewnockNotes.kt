package content.region.kandarin.gnome.quest.pog.book

import content.global.plugin.iface.BookInterface
import content.global.plugin.iface.BookLine
import content.global.plugin.iface.Page
import content.global.plugin.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class YewnockNotes : InteractionListener {

    /*
     * Yewnock's notes is obtained
     * during The Path of Glouphrie quest.
     */

    companion object {
        private val TITLE = "Yewnock's notes"
        private val CONTENTS = arrayOf(
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
                    BookLine("persuade creatures as long-", 77),
                    BookLine("lived as elves to get to the", 78),
                    BookLine("point! Father has shown that", 79),
                    BookLine("small crystal seeds may be", 80),
                    BookLine("grown into more than one", 81),
                ),
                Page(
                    BookLine("enchanted artefact. This is a", 82),
                    BookLine("very different means of", 83),
                    BookLine("crafting magical items than is", 84),
                    BookLine("known to most gnomes. It", 85),
                    BookLine("It seems to be that, using a", 86),
                    BookLine("static object such as the", 87),
                    BookLine("chanting mechanism (like the", 88),
                    BookLine("singing bowls), can only", 89),
                    BookLine("produce one type of item -", 90),
                    BookLine("the pitch of the bowl's ring", 91),
                    BookLine("defines the nature of the", 92),
                    BookLine("crystal object it forms. Since", 93),
                    BookLine("some elves can use their own", 94),
                    BookLine("voices to mould crystal, they", 95),
                    BookLine("can modulate their chanting", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("to create many different", 97),
                    BookLine("items. Father has heard these", 68),
                    BookLine("and describes them as the", 69),
                    BookLine("most beautiful songs he has", 70),
                    BookLine("ever heard - it must be", 71),
                    BookLine("amazing to hear a true", 72),
                    BookLine("chanter sing. I suspect this is", 73),
                    BookLine("an ability the elves keep to", 74),
                    BookLine("themselves. Us mortals must", 75),
                    BookLine("rely on these static crystal-", 76),
                    BookLine("shaping devices to sing for", 77),
                    BookLine("us. It also seems clear that if", 78),
                    BookLine("you 'overcharge' a crystal", 79),
                    BookLine("item by producing the same", 80),
                    BookLine("chant, except much louder", 81),
                ),
                Page(
                    BookLine("than normal, you can revert", 82),
                    BookLine("the crystal to seed. This will", 83),
                    BookLine("happen anyway if the magic", 84),
                    BookLine("is drained away. My father", 85),
                    BookLine("also seemed to think that", 86),
                    BookLine("different-sized seeds were", 87),
                    BookLine("used by elves to do different", 88),
                    BookLine("things. The larger ones", 89),
                    BookLine("were used for weapons, and", 90),
                    BookLine("the smaller ones for tools.", 91),
                    BookLine("Father had one of the small", 92),
                    BookLine("ones that he had turned into", 93),
                    BookLine("a crystal saw; very useful.", 94),
                    BookLine("He seemed to think the same", 95),
                    BookLine("seed could be turned into", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("other similarly-sized devices", 97),
                    BookLine("with the right chant. One", 68),
                    BookLine("other point he notes - since", 69),
                    BookLine("the singing bowl is also elf", 70),
                    BookLine("crystal - if you knew how to", 71),
                    BookLine("create the right song, you", 72),
                    BookLine("could use a singing bowl to", 73),
                    BookLine("make another singing bowl.", 74),
                    BookLine("One could also craft some", 75),
                    BookLine("other type of artefact or", 76),
                    BookLine("device that could itself chant,", 77),
                    BookLine("a musical instrument for", 78),
                    BookLine("example, though what other", 79),
                    BookLine("uses the chant could have", 80),
                    BookLine("apart from moulding crystal", 81),
                ),
                Page(
                    BookLine("was still unknown to my", 82),
                    BookLine("father, though I believe he", 83),
                    BookLine("may have done some", 84),
                    BookLine("experimentation on the", 85),
                    BookLine("matter.", 86),
                ),
            ),
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun display(player: Player, pageNum: Int, buttonID: Int): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, TITLE, CONTENTS)
        return true
    }

    override fun defineListeners() {
        on(Items.YEWNOCKS_NOTES_11750, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}
