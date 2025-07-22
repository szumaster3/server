package content.global.plugin.item.book.guide

import content.global.plugin.iface.BookInterface
import content.global.plugin.iface.BookLine
import content.global.plugin.iface.Page
import content.global.plugin.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs.consts.Items

class GlassblowingGuide : InteractionListener {

    /*
     * The Glassblowing book is an item that players can obtain from
     * the reception at the Observatory north of Castle Wars, or from
     * bookcase on the first floor of the house near Fritz's on Entrana.
     */

    companion object {
        private const val TITLE = "Ultimate Guide to Glassblowing"

        private const val BUCKET_MODEL = 2397
        private const val GLASSBLOWING_PIPE_MODEL = 2570
        private const val SEAWEED_MODEL = 2370
        private const val SODA_ASH_MODEL = 2742

        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("<col=08088A>Ultimate Guide to</col>", 55),
                    BookLine("<col=08088A>Glassblowing</col>", 56),
                    BookLine("", 57),
                    BookLine("Author: Fritz the", 58),
                    BookLine("Glassblower", 59),
                    BookLine("", 60),
                    BookLine("I can tell you now, the", 61),
                    BookLine("art of creating glass has", 62),
                    BookLine("long been a valued trade", 63),
                    BookLine("in ${GameWorld.settings?.name}. Not many", 64),
                    BookLine("could get by without the", 65),
                ),
                Page(
                    BookLine("use of some item made", 66),
                    BookLine("from this marvelous", 67),
                    BookLine("substance.", 68),
                    BookLine("It's within this book that", 70),
                    BookLine("we cover the basics of the", 71),
                    BookLine("art, in a hope to get you", 72),
                    BookLine("hooked on this popular", 73),
                    BookLine("pass-time and trade.", 74),
                    BookLine("To start with, you will", 75),
                    BookLine("need a few essential", 76),
                ),
            ),
            PageSet(
                Page(
                    BookLine("items:", 55),
                    /*
                     * BUCKET MODEL (56-58).
                     */
                    BookLine("    <col=08088A>An empty bucket.</col>", 59),
                    BookLine("", 60),
                    BookLine("These can be purchased", 61),
                    BookLine("from the majority of", 62),
                    BookLine("general stores and are", 63),
                    BookLine("required to carry the", 64),
                    BookLine("sand you will need to", 65),
                ),
                Page(
                    BookLine("make glass.", 66),
                    /*
                     * PIPE MODEL (67-69).
                     */
                    BookLine("    <col=08088A>A glassblowing pipe</col>", 70),
                    BookLine("", 71),
                    BookLine("found on Entrana or", 72),
                    BookLine("often just north-west of", 73),
                    BookLine("the Ranging Guild. As it's", 74),
                    BookLine("name suggests, this is", 75),
                    BookLine("used to blow air into", 76),
                ),
            ),
            PageSet(
                Page(
                    BookLine("molten glass.", 55),
                    /*
                     * SEAWEED MODEL (56-57).
                     */
                    BookLine("         <col=08088A>Seaweed</col>", 59),
                    BookLine("", 60),
                    BookLine("Seaweed can be found on", 61),
                    BookLine("the shore in many places,", 62),
                    BookLine("but it is also a frequent", 63),
                    BookLine("find when Fishing with", 64),
                    BookLine("nets. This you will need", 65),
                ),
                Page(
                    BookLine("to create soda ash - an", 66),
                    BookLine("important ingredient to", 67),
                    BookLine("the process.", 68),
                    /*
                     * SODA ASH MODEL (69-71).
                     */
                    BookLine("         <col=08088A>Soda ash</col>", 72),
                    BookLine("", 73),
                    BookLine("Made by burning", 74),
                    BookLine("seaweed, you will need", 75),
                    BookLine("this to make glass. Other", 76),
                ),
            ),
            PageSet(
                Page(
                    BookLine("than making it yourself,", 55),
                    BookLine("you can purchase it from", 56),
                    BookLine("other budding", 57),
                    BookLine("glassblowers.", 58),
                    BookLine("", 59),
                    BookLine("With these items you are", 60),
                    BookLine("ready to begin! You'll", 61),
                    BookLine("need to find a sandpit,", 62),
                    BookLine("and there are a few", 63),
                    BookLine("dotted around", 64),
                    BookLine("${GameWorld.settings?.name}. Try the", 65),
                ),
                Page(
                    BookLine("island of Entrana or", 66),
                    BookLine("Yanille, for example. Use", 67),
                    BookLine("your bucket on the", 68),
                    BookLine("sandpit to fill it up.", 69),
                    BookLine("", 70),
                    BookLine("Next, you need to find", 71),
                    BookLine("your way to a furnace.", 72),
                    BookLine("Turn your seaweed into", 73),
                    BookLine("soda ash by using it on a", 74),
                    BookLine("range before using the", 75),
                    BookLine("bucket of sand or soda", 76),
                ),
            ),
            PageSet(
                Page(
                    BookLine("ash on the furnace to", 55),
                    BookLine("create molten glass.", 56),
                    BookLine("", 57),
                    BookLine("The last stage is simply", 58),
                    BookLine("to use the molten glass", 59),
                    BookLine("with your glassblowing", 60),
                    BookLine("pipe and, depending on", 61),
                    BookLine("your level of skill, you", 62),
                    BookLine("can create a variety of", 63),
                    BookLine("items.", 64),
                ),
                Page(
                    BookLine("I hope you enjoyed this", 66),
                    BookLine("guide, and please keep an", 67),
                    BookLine("eye out for more of my", 68),
                    BookLine("titles!", 69),
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
        /*
         * Bucket model.
         */
        BookInterface.setModelOnPage(player, 1, BUCKET_MODEL, BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[3], BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[3], 500, 200, 0)
        /*
         * Pipe model.
         */
        BookInterface.setModelOnPage(player, 1, GLASSBLOWING_PIPE_MODEL, BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[13], BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[13], 600, 200, 100)
        /*
         * Seaweed model.
         */
        BookInterface.setModelOnPage(player, 2, SEAWEED_MODEL, BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[2], BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[2], 900, 500, 100)
        /*
         * Soda ash model.
         */
        BookInterface.setModelOnPage(player, 2, SODA_ASH_MODEL, BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_IMAGE_ENABLE_DRAW_IDS[15], BookInterface.FANCY_BOOK_3_49_IMAGE_DRAW_IDS[15], 600, 500, 100)
        return true
    }

    override fun defineListeners() {
        on(Items.GLASSBLOWING_BOOK_11656, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
