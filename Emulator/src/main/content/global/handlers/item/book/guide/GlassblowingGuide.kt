package content.global.handlers.item.book.guide

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs.consts.Items

class GlassblowingGuide : InteractionListener {
    /*
     * The glassblowing book is a book that players can obtain from the
     * reception at the Observatory north of Castle Wars.
     */

    companion object {
        private const val TITLE = "Ultimate Guide to Glassblowing"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Ultimate Guide to", 55),
                        BookLine("<col=08088A>Glassblowing Author:", 56),
                        BookLine("<col=08088A>Fritz the Glassblower", 57),
                        BookLine("I can tell you now, the", 59),
                        BookLine("art of creating glass", 60),
                        BookLine("has long been a valued", 61),
                        BookLine("trade in " + GameWorld.settings!!.name + ".", 62),
                        BookLine("Not many could get by", 63),
                        BookLine("without the use of some", 64),
                        BookLine("item made from this", 65),
                    ),
                    Page(
                        BookLine("marvelous substance. It's", 66),
                        BookLine("within this book that", 67),
                        BookLine("we cover the basics", 68),
                        BookLine("of the art, in a hope", 69),
                        BookLine("to get you hooked on", 70),
                        BookLine("this popular pass-time", 71),
                        BookLine("and trade. To start", 72),
                        BookLine("with, you will need", 73),
                        BookLine("a few essential items:", 74),
                        BookLine("An empty bucket.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("These can be purchased", 55),
                        BookLine("from the majority of", 56),
                        BookLine("general stores and", 57),
                        BookLine("are required to carry", 58),
                        BookLine("the sand you will need", 59),
                        BookLine("to make glass.", 60),
                        BookLine("<col=08088A>A glassblowing pipe", 62),
                        BookLine("found on Entrana or often", 63),
                        BookLine("just north-west of the", 64),
                        BookLine("Ranging Guild. As its", 65),
                    ),
                    Page(
                        BookLine("name suggests, this", 66),
                        BookLine("is used to blow air", 67),
                        BookLine("into molten glass.", 68),
                        BookLine("<col=08088A>Seaweed", 70),
                        BookLine("can be found on the", 71),
                        BookLine("shore in many places,", 72),
                        BookLine("but it is also a frequent", 73),
                        BookLine("find when Fishing with", 74),
                        BookLine("nets. This you will", 75),
                        BookLine("need to create soda", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("ash - an important", 55),
                        BookLine("ingredient to the process.", 56),
                        BookLine("<col=08088A>Soda ash", 58),
                        BookLine("Made by burning", 59),
                        BookLine("seaweed, you will need", 60),
                        BookLine("this to make glass.", 61),
                        BookLine("Other than making it", 62),
                        BookLine("yourself, you can purchase", 63),
                        BookLine("from other budding", 64),
                        BookLine("glassblowers.", 65),
                    ),
                    Page(
                        BookLine("With these items you are", 66),
                        BookLine("ready to begin! You'll", 67),
                        BookLine("need to find a sandpit", 68),
                        BookLine("and there are a few", 69),
                        BookLine("dotted around " + GameWorld.settings!!.name + ".", 70),
                        BookLine("Try the isle of Entrana", 71),
                        BookLine("or Yanille, for example.", 72),
                        BookLine("Use your bucket on the", 73),
                        BookLine("sandpit to fill it up.", 74),
                        BookLine("Next, you need to find", 75),
                        BookLine("your way to a furnace.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Turn your seaweed into", 55),
                        BookLine("soda ash by using it", 56),
                        BookLine("on a range before using a", 57),
                        BookLine("bucket of sand or soda", 58),
                        BookLine("ash on the furnace to", 59),
                        BookLine("create molten glass. The", 60),
                        BookLine("last stage is simply", 61),
                        BookLine("to use the molten glass", 62),
                        BookLine("wth your glassblowing", 63),
                        BookLine("pipe and, depending", 64),
                    ),
                    Page(
                        BookLine("on your level of skill,", 66),
                        BookLine("you can create a variety", 67),
                        BookLine("of items.", 68),
                        BookLine("I hope you enjoyed this", 70),
                        BookLine("guide, and please keep", 71),
                        BookLine("an eye out for more", 72),
                        BookLine("of my titles!", 73),
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
        on(Items.GLASSBLOWING_BOOK_11656, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
