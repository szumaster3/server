package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class BirdBook : InteractionListener {
    /*
     * Find as part of the Eagles' Peak quest.
     * It is written by William Oddity, an expert on hunting.
     */

    companion object {
        private const val TITLE = "William Oddity's Guide to the Avian"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>Crimson Swift", 55),
                        BookLine("The crimson swift is a", 57),
                        BookLine("tropical bird that", 58),
                        BookLine("typically lives in", 59),
                        BookLine("dense jungle areas.", 60),
                        BookLine("Subtlety is not one", 61),
                        BookLine("of this birds", 62),
                        BookLine("strong points,its", 63),
                        BookLine("striking red plumage", 64),
                        BookLine("ensuring it will be", 65),
                    ),
                    Page(
                        BookLine("visible wherever it", 66),
                        BookLine("goes,making it a popular", 67),
                        BookLine("first bird for hunters", 68),
                        BookLine("and spotters alike.", 69),
                        BookLine("<col=08088A>Cerulean Twitch", 71),
                        BookLine("This polar bird,", 73),
                        BookLine("found in the far", 74),
                        BookLine("Northern regions,can", 75),
                        BookLine("survive at impressively", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("low temperatures. Although", 55),
                        BookLine("its long tail affords", 56),
                        BookLine("it great manoeuvrability,", 57),
                        BookLine("it is also the cause", 58),
                        BookLine("of a great deal of", 59),
                        BookLine("interest from collectors.", 60),
                        BookLine("<col=08088A>Golden Warbler", 62),
                        BookLine("The golden warbler", 64),
                        BookLine("can be easily identified", 65),
                    ),
                    Page(
                        BookLine("by its hideously", 66),
                        BookLine("annoying song. Although", 67),
                        BookLine("the warbler is prized", 68),
                        BookLine("by hunters for its", 69),
                        BookLine("fine feathers,it is", 70),
                        BookLine("probably caught just", 71),
                        BookLine("as much simply to shut", 72),
                        BookLine("it up.", 73),
                        BookLine("<col=08088A>Tropical Wagtail", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("This beautiful bird can", 55),
                        BookLine("be found in hot jungle", 56),
                        BookLine("climates where it expertly", 57),
                        BookLine("catches small insects", 58),
                        BookLine("for food. It is perhaps", 59),
                        BookLine("therefore ironic that", 60),
                        BookLine("it is usually caught", 61),
                        BookLine("for its bright plumage,", 62),
                        BookLine("which can be used in", 63),
                        BookLine("turn to make excellent", 64),
                        BookLine("fly fishing lures.", 65),
                    ),
                    Page(
                        BookLine("<col=08088A>Copper Longtail", 66),
                        BookLine("The copper longtail", 68),
                        BookLine("is found in temperate", 69),
                        BookLine("wooded regions and", 70),
                        BookLine("is one of the most", 71),
                        BookLine("common birds in the", 72),
                        BookLine("world. Although its", 73),
                        BookLine("appearance is unremarkable", 74),
                        BookLine("it is a graceful and", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("successful bird that", 55),
                        BookLine("continues to fend well", 56),
                        BookLine("for itself.", 57),
                        BookLine("<col=08088A>Giant Eagle", 59),
                        BookLine("Little is known about", 61),
                        BookLine("the giant eagles.", 62),
                        BookLine("There are reported", 63),
                        BookLine("sightings in many parts", 64),
                        BookLine("of the world,but the", 65),
                    ),
                    Page(
                        BookLine("nests of these magnificent", 66),
                        BookLine("creatures remain elusive.", 67),
                        BookLine("The only record of", 68),
                        BookLine("such a nest being found", 69),
                        BookLine("is in the folk tale", 70),
                        BookLine("of the bandits of the", 71),
                        BookLine("golden claw. It is", 72),
                        BookLine("said that the bandits", 73),
                        BookLine("leader could talk to", 74),
                        BookLine("and befriend the eagles,", 75),
                        BookLine("and used the eagles'", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("nest at the top of", 55),
                        BookLine("what is now known as", 56),
                        BookLine("eagles' peak as a hideout.", 57),
                        BookLine("The story goes on to", 58),
                        BookLine("hint at a secret entrance", 59),
                        BookLine("to the hideaway existing", 60),
                        BookLine("near the peak of the", 61),
                        BookLine("mountain,but until", 62),
                        BookLine("this day it has not", 63),
                        BookLine("been found.", 64),
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
        on(Items.BIRD_BOOK_10173, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
