package content.global.plugins.item.books_and_scrolls.guide

import content.global.plugins.iface.BookInterface
import content.global.plugins.iface.BookLine
import content.global.plugins.iface.Page
import content.global.plugins.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import shared.consts.Items

class ArdougneGuide : InteractionListener {

    /*
     * Informational book found in the real estate agency
     * near the southern bank of East Ardougne.
     * Authentic state.
     */
    companion object {
        private val TITLE = "Tourist Guide to Ardougne"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=ff0000>Introduction.</col>", 55),
                        BookLine("",                                56),
                        BookLine("This book is your guide",         57),
                        BookLine("to the vibrant city of",          58),
                        BookLine("Ardougne.",                       59),
                        BookLine("Ardougne is known as an",         60),
                        BookLine("exciting modern city",            61),
                        BookLine("located on the sun",              62),
                        BookLine("drenched southern coast of",      63),
                        BookLine("Kandarin.",                       64),
                    ),
                    Page(
                        BookLine("<col=ff0000>Ardougne: City of</col>",  66),
                        BookLine("<col=ff0000>Shopping!</col>",          67),
                        BookLine("Come sample the delights",             68),
                        BookLine("of the Ardougne market ",              69),
                        BookLine("the biggest in the known",             70),
                        BookLine("world! From spices to silk,",          71),
                        BookLine("there is something here",              72),
                        BookLine("for everybody! Other",                 73),
                        BookLine("popular shopping",                     74),
                        BookLine("destinations in the area",             75),
                        BookLine("include the Armoury and",              76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("the ever popular",                            55),
                        BookLine("Adventurers' supply store.",                  56),
                        BookLine("",                                            57),
                        BookLine("<col=ff0000>Ardougne: City of Fun!</col>",    58),
                        BookLine("If you're looking for",                       59),
                        BookLine("entertainment in",                            60),
                        BookLine("Ardougne, why not pay a",                     61),
                        BookLine("visit to the Ardougne City",                  62),
                        BookLine("Zoo? Or relax with a",                        63),
                        BookLine("drink in the ever popular",                   64),
                        BookLine("Flying Horse Inn?",                           65),
                    ),
                    Page(
                        BookLine("And for the adventurous,",                 66),
                        BookLine("there are always rats to",                 67),
                        BookLine("be slaughtered in",                        68),
                        BookLine("Ardougnes expansive and",                  69),
                        BookLine("vermin-ridden sewers.",                    70),
                        BookLine("There is truly something",                 71),
                        BookLine("for everybody here!",                      72),
                        BookLine("",                                         73),
                        BookLine("<col=ff0000>Ardougne: City of</col>",      74),
                        BookLine("<col=ff0000>History!</col>",               75),
                        BookLine("Ardougne is renowned as",                  76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("an important city of",        55),
                        BookLine("historical interest. One",    56),
                        BookLine("historic building is the",    57),
                        BookLine("magnificent Handelmort",      58),
                        BookLine("Mansion, currently",          59),
                        BookLine("owned by Lord Francis",       60),
                        BookLine("Kurt Handelmort. Also of",    61),
                        BookLine("historical interest is",      62),
                        BookLine("Ardougne Castle in the",      63),
                        BookLine("East side of the city,",      64),
                        BookLine("recently opened to the",      65),
                    ),
                    Page(
                        BookLine("public. And of course, the",            66),
                        BookLine("Holy Order of Paladins",                67),
                        BookLine("still wander the streets of",           68),
                        BookLine("Ardougne, and are often",               69),
                        BookLine("of interest to tourists.",              70),
                        BookLine("",                                      71),
                        BookLine("<col=ff0000>Further Fields</col>",      72),
                        BookLine("The area surrounding",                  73),
                        BookLine("Ardougne is also of great",             74),
                        BookLine("interest to the cultural",              75),
                        BookLine("tourist.",                              76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("If you want to go",                   55),
                        BookLine("further afield, why not",             56),
                        BookLine("have a look at the",                  57),
                        BookLine("ominous Pillars of",                  58),
                        BookLine("Zanash, the mysterious",              59),
                        BookLine("marble pillars located just",         60),
                        BookLine("West of the city?",                   61),
                        BookLine("Or perhaps the town of",              62),
                        BookLine("Brimhaven, on the exotic",            63),
                        BookLine("island of Karamja? It's",             64),
                        BookLine("only a short boat trip,",             65),
                    ),
                    Page(
                        BookLine("with regular transport",            66),
                        BookLine("leaving from Ardougne",             67),
                        BookLine("Harbour at all times of",           68),
                        BookLine("the day, all year round.",          69),
                    )
                )
            )


        @Suppress("UNUSED_PARAMETER")
        private fun display(
            player: Player,
            pageNum: Int,
            buttonID: Int,
        ): Boolean {
            BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, CONTENTS)
            return true
        }
    }

    override fun defineListeners() {
        on(Items.GUIDE_BOOK_1856, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
            return@on true
        }
    }
}
