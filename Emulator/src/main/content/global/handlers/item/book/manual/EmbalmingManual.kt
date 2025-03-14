package content.global.handlers.item.book.manual

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class EmbalmingManual : InteractionListener {
    /*
     * The book is used in the Icthlarin's Little Helper quest.
     */

    companion object {
        private const val TITLE = "Embalming Manual"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=FF0000>Embalming is an essential", 55),
                        BookLine("part of Menaphite funerary", 56),
                        BookLine("practice. It is believed", 57),
                        BookLine("that the spirits of the", 58),
                        BookLine("deceased return to their", 59),
                        BookLine("bodies to find sustenance,", 60),
                        BookLine("however if their body", 61),
                        BookLine("decays or becomes unrecognisable", 62),
                        BookLine("then the spirit goes hungry", 63),
                        BookLine("and their afterlife is", 64),
                        BookLine("jeopardized. As an embalmer", 65),
                    ),
                    Page(
                        BookLine("we seek to prevent this", 66),
                        BookLine("decay and offer our clients", 67),
                        BookLine("the best afterlife that", 68),
                        BookLine("money can pay for. As", 69),
                        BookLine("we are of a scientific", 70),
                        BookLine("persuasion I will only", 71),
                        BookLine("deal with the technicalities", 72),
                        BookLine("of the job. Prayers and", 73),
                        BookLine("other religious ceremonies", 74),
                        BookLine("will be left the priests", 75),
                        BookLine("and acolytes of Icthlarin.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=FF0000>The Process:", 55),
                        BookLine("1: Remove the body's", 56),
                        BookLine("large internal organs", 57),
                        BookLine("2: Apply preservatives", 58),
                        BookLine("to the corpse", 59),
                        BookLine("3: Wrap corpse in", 60),
                        BookLine("bandages", 61),
                        BookLine("4: Seal bandages", 62),
                        BookLine("5: Give final blessing", 63),
                        BookLine("<col=FF0000>1: Removal of large", 64),
                        BookLine("<col=FF0000>organs", 65),
                    ),
                    Page(
                        BookLine("Incisions are made in", 66),
                        BookLine("the body to remove most", 67),
                        BookLine("of the large organs.", 68),
                        BookLine("These are then placed", 69),
                        BookLine("inside large canopic", 70),
                        BookLine("jars. Each jar is", 71),
                        BookLine("blessed and put under", 72),
                        BookLine("the protection of", 73),
                        BookLine("one of Icthlarin's", 74),
                        BookLine("minions.", 75),
                        BookLine("<col=FF0000>2: Application of", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=FF0000>preservatives", 55),
                        BookLine("Salts are applied to", 56),
                        BookLine("the body in order to", 57),
                        BookLine("dry out the body and", 58),
                        BookLine("reduce decay. Salt is", 59),
                        BookLine("a valuable spice but", 60),
                        BookLine("thankfully it can be", 61),
                        BookLine("easily obtained in the", 62),
                        BookLine("Sophanem area. However", 63),
                        BookLine("the process of creating", 64),
                        BookLine("it is slightly complicated.", 65),
                    ),
                    Page(
                        BookLine("A small salt water lake", 66),
                        BookLine("exists to the north of", 67),
                        BookLine("Sophanem, water from", 68),
                        BookLine("this source can be evaporated", 69),
                        BookLine("off leaving a residue", 70),
                        BookLine("of salt. Natural evaporation", 71),
                        BookLine("is a long and time consuming", 72),
                        BookLine("process. At the time", 73),
                        BookLine("of writing of this book", 74),
                        BookLine("a sun focusing device", 75),
                        BookLine("is being constructed", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("in the south of Sophanem", 55),
                        BookLine("in order to speed the", 56),
                        BookLine("crystallisation process", 57),
                        BookLine("up.", 58),
                        BookLine("<col=FF0000>3: Wrap corpse", 59),
                        BookLine("<col=FF0000>in bandages", 60),
                        BookLine("For our executive customers", 61),
                        BookLine("we have added a new stage", 62),
                        BookLine("to the embalming process", 63),
                        BookLine("- that is wrapping them", 64),
                        BookLine("up in linen. This further", 65),
                    ),
                    Page(
                        BookLine("decreases the decay of", 66),
                        BookLine("the body. Linen should", 67),
                        BookLine("be available from any", 68),
                        BookLine("half decent", 69),
                        BookLine("cloth merchant", 70),
                        BookLine("in the area as it is", 71),
                        BookLine("the cloth of choice for", 72),
                        BookLine("most Menaphites.", 73),
                        BookLine("<col=FF0000>4: Seal bandages", 74),
                        BookLine("To seal in all the bodies", 75),
                        BookLine("goodness sap is essential.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The body can either be", 55),
                        BookLine("covered in this or the", 56),
                        BookLine("linen bandages. Sap is", 57),
                        BookLine("a sought after", 58),
                        BookLine("and expensive", 59),
                        BookLine("commodity for those who", 60),
                        BookLine("live in desert climes.", 61),
                        BookLine("This is because it is", 62),
                        BookLine("only obtainable from", 63),
                        BookLine("evergreen trees whose", 64),
                        BookLine("natural habitat exists", 65),
                    ),
                    Page(
                        BookLine("far from the harsh dry", 66),
                        BookLine("desert climes. The smart", 67),
                        BookLine("embalmer should always", 68),
                        BookLine("try horde sap in times", 69),
                        BookLine("of plenty as they have", 70),
                        BookLine("little time to", 71),
                        BookLine("be travelling", 72),
                        BookLine("to distant lands. The", 73),
                        BookLine("process of collecting", 74),
                        BookLine("sap is a simple one though", 75),
                        BookLine("and all that is needed", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("is a knife and an empty", 55),
                        BookLine("bucket. A small cut should", 56),
                        BookLine("be made on the tree with", 57),
                        BookLine("the knife and the sap", 58),
                        BookLine("collected with the bucket.", 59),
                        BookLine("<col=FF0000>5: Give final blessing", 60),
                        BookLine("The priests of Icthlarin", 61),
                        BookLine("then finish the process", 62),
                        BookLine("by carrying out a final", 63),
                        BookLine("ceremony where the", 64),
                        BookLine("deceased spirit", 65),
                    ),
                    Page(
                        BookLine("departs their", 66),
                        BookLine("body and leaves for the", 67),
                        BookLine("afterlife. The details", 68),
                        BookLine("of this are of little", 69),
                        BookLine("consequences to", 70),
                        BookLine("the embalmer. By", 71),
                        BookLine("Bod E Wrapper", 72),
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
        on(Items.EMBALMING_MANUAL_4686, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
