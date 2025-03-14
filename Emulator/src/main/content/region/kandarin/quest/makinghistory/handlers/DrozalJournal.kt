package content.region.kandarin.quest.makinghistory.handlers

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class DrozalJournal : InteractionListener {
    companion object {
        private val TITLE = "Drozal's Journal"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("----------------------", 55),
                        BookLine("---1st Bennath  ", 56),
                        BookLine("----------------------", 57),
                        BookLine("My life is full of changes", 58),
                        BookLine("at the moment. I've met", 59),
                        BookLine("a great and powerful follower", 60),
                        BookLine("of Zamorak, whom has offered", 61),
                        BookLine("me the chance to join", 62),
                        BookLine("him and others in the", 63),
                        BookLine("quest for ultimate power", 64),
                        BookLine("and desolation of others.", 65),
                    ),
                    Page(
                        BookLine("This is just the opportunity", 66),
                        BookLine("I've been looking for!", 67),
                        BookLine("I move into the old outpost", 68),
                        BookLine("(North of Ardougne) at", 69),
                        BookLine("the end of this week. I", 70),
                        BookLine("can hardly wait!", 71),
                        BookLine("----------------------", 72),
                        BookLine("---15th Bennath", 73),
                        BookLine("----------------------", 74),
                        BookLine("Been here a few days now.", 75),
                        BookLine("I'm overwhelmed on just", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("how evil the other 8 are.", 55),
                        BookLine("I suppose this is amplified", 56),
                        BookLine("by living in such a small", 57),
                        BookLine("building.", 58),
                        BookLine("----------------------", 59),
                        BookLine("---20th Bennath", 60),
                        BookLine("----------------------", 61),
                        BookLine("Started causing havoc", 62),
                        BookLine("to the people of Ardougne", 63),
                        BookLine("today. We poisoned the", 64),
                        BookLine("water supply with a strange", 65),
                    ),
                    Page(
                        BookLine("concoction which caused", 66),
                        BookLine("everyone to break out", 67),
                        BookLine("in boils! Very funny.", 68),
                        BookLine("The others have been stealthily", 69),
                        BookLine("setting fire to some people", 70),
                        BookLine("and laughing at their", 71),
                        BookLine("conclusion of spontaneous", 72),
                        BookLine("combustion. I think I", 73),
                        BookLine("shall have to come up", 74),
                        BookLine("with something more evil", 75),
                        BookLine("for tomorrow.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("----------------------", 55),
                        BookLine("---32nd Bennath", 56),
                        BookLine("----------------------", 57),
                        BookLine("Following my genius plan", 58),
                        BookLine("to make all the children", 59),
                        BookLine("invisible, the city is", 60),
                        BookLine("now in total chaos as", 61),
                        BookLine("the kids test their freedom", 62),
                        BookLine("by putting adults into", 63),
                        BookLine("a panic. Honestly, some", 64),
                        BookLine("of the tricks the kids", 65),
                    ),
                    Page(
                        BookLine("play are better than anything", 66),
                        BookLine("I could invent.", 67),
                        BookLine("----------------------", 68),
                        BookLine("---20th Raktuber", 69),
                        BookLine("----------------------", 70),
                        BookLine("adly things have calmed", 71),
                        BookLine("down in the city, and", 72),
                        BookLine("they've started to realise", 73),
                        BookLine("that we're the cause of", 74),
                        BookLine("all the tragedy. Luckily,", 75),
                        BookLine("the people don't have", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("enough power to get past", 55),
                        BookLine("our defences, and even", 56),
                        BookLine("if they did, they would", 57),
                        BookLine("wish they hadn't thought", 58),
                        BookLine("of such an attack.", 59),
                        BookLine("----------------------", 60),
                        BookLine("---28th Raktuber", 61),
                        BookLine("----------------------", 62),
                        BookLine("We have been told to 'beware", 63),
                        BookLine("for your days are numbered'", 64),
                        BookLine("by the people of Ardougne.", 65),
                    ),
                    Page(
                        BookLine("It seems the city has", 66),
                        BookLine("asked for some external", 67),
                        BookLine("help, which will be upon", 68),
                        BookLine("us some time soon. I'm", 69),
                        BookLine("sure we will eliminate", 70),
                        BookLine("such a threat though.", 71),
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
        on(Items.JOURNAL_6755, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
