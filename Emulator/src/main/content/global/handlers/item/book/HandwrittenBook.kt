package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class HandwrittenBook : InteractionListener {
    /*
     * Obtainable while The Eyes of Glouphrie quest.
     */

    companion object {
        private const val TITLE = "The Eyes of Glouphrie"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Preface This book is", 55),
                        BookLine("written to preserve", 56),
                        BookLine("the knowledge of crystal", 57),
                        BookLine("singing. It is by no", 58),
                        BookLine("means meant to cover", 59),
                        BookLine("the advanced concepts", 60),
                        BookLine("required to carry out", 61),
                        BookLine("a chant.", 62),
                        BookLine("<col=08088A>Chapter 1", 64),
                        BookLine("What is a Crystal seed?", 65),
                    ),
                    Page(
                        BookLine("As you may know all", 66),
                        BookLine("crystal equipment is", 67),
                        BookLine("grown from a crystal", 68),
                        BookLine("seed. You may not know", 69),
                        BookLine("that all crystal seeds", 70),
                        BookLine("come from the same", 71),
                        BookLine("place; the ancestral", 72),
                        BookLine("home of Seren.", 73),
                        BookLine("Reading the Cerddi tells", 74),
                        BookLine("us that crystal seed", 75),
                        BookLine("is sentient, although", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("no-one has ever been", 55),
                        BookLine("able to converse with", 56),
                        BookLine("any crystal consciousness", 57),
                        BookLine("other than that of Seren.", 58),
                        BookLine("One of the ways we can", 59),
                        BookLine("see this consciousness", 60),
                        BookLine("in action is in the", 61),
                        BookLine("way that crystal equipment,", 62),
                        BookLine("when first used, becomes", 63),
                        BookLine("aligned with that person.", 64),
                    ),
                    Page(
                        BookLine("<col=08088A>Chapter 2", 66),
                        BookLine("Crystal chanting As", 67),
                        BookLine("far as we can tell there", 68),
                        BookLine("have always been those", 69),
                        BookLine("with the ability to", 70),
                        BookLine("chant crystal, these", 71),
                        BookLine("tend to be from the", 72),
                        BookLine("Ithell family, but it", 73),
                        BookLine("has been known for these", 74),
                        BookLine("'Crystal Chanters' to", 75),
                        BookLine("be born to other families.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The thing that makes", 55),
                        BookLine("these 'Crystal Chanters'", 56),
                        BookLine("special is their ability", 57),
                        BookLine("to vocalise perfect", 58),
                        BookLine("pitches beyond that", 59),
                        BookLine("which humans can hear.", 60),
                        BookLine("The idea behind chanting", 61),
                        BookLine("is quite simple; different", 62),
                        BookLine("pitches can change the", 63),
                        BookLine("shape and size of any", 64),
                        BookLine("facet of the seed. This", 65),
                    ),
                    Page(
                        BookLine("allows a practiced Chanter", 66),
                        BookLine("to make any number of", 67),
                        BookLine("shapes. It is also possible", 68),
                        BookLine("for particularly gifted", 69),
                        BookLine("individuals to imbue", 70),
                        BookLine("powers into these objects.", 71),
                        BookLine("The only problem with", 72),
                        BookLine("this process is that", 73),
                        BookLine("it is very costly and", 74),
                        BookLine("time consuming. The", 75),
                        BookLine("process can, however,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("be made more economical:", 55),
                        BookLine("the better the Chanter", 56),
                        BookLine("knows the person for", 57),
                        BookLine("whom they are creating", 58),
                        BookLine("items, the easier it", 59),
                        BookLine("is to make.", 60),
                        BookLine("<col=08088A>Chapter 3", 62),
                        BookLine("Crystal properties", 63),
                        BookLine("Although crystal seeds", 64),
                        BookLine("are highly convenient", 65),
                    ),
                    Page(
                        BookLine("they do have some", 66),
                        BookLine("drawbacks, the foremost", 67),
                        BookLine("being their brittleness.", 68),
                        BookLine("This will mean that any", 69),
                        BookLine("equipment made from a", 70),
                        BookLine("crystal seed will get", 71),
                        BookLine("weaker as it takes damage", 72),
                        BookLine("until eventually it reverts", 73),
                        BookLine("back to its original", 74),
                        BookLine("form, which can be reused", 75),
                        BookLine("to create any new crystal", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("equipment. Unfortunately", 55),
                        BookLine("it is not possible to", 56),
                        BookLine("repair any Crystal items;", 57),
                        BookLine("it will need to return", 58),
                        BookLine("to seed before it can", 59),
                        BookLine("be made whole again.", 60),
                        BookLine("<col=08088A>Crystal Seed", 62),
                        BookLine("is one of our most", 63),
                        BookLine("valuable assets but", 64),
                        BookLine("with the loss of the", 65),
                    ),
                    Page(
                        BookLine("'World Gate' in the", 66),
                        BookLine("2nd age access to this", 67),
                        BookLine("resource has been lost.", 68),
                        BookLine("Luckily there is no", 69),
                        BookLine("known way to destroy", 70),
                        BookLine("a crystal seed.", 71),
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
        on(Items.A_HANDWRITTEN_BOOK_9627, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
