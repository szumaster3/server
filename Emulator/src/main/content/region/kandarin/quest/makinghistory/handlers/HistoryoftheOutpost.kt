package content.region.kandarin.quest.makinghistory.handlers

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.node.entity.player.Player

class HistoryoftheOutpost {
    companion object {
        private val TITLE = "The History of the Outpost"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Approximately 140 years", 55),
                        BookLine("after the establishment", 56),
                        BookLine("of the outpost, a great", 57),
                        BookLine("traveller, known only as", 58),
                        BookLine("Player unravelled the", 59),
                        BookLine("history of the outpost.", 60),
                        BookLine("", 61),
                        BookLine("It transpires that the", 62),
                        BookLine("outpost was first established", 63),
                        BookLine("to protect the nearby city", 64),
                        BookLine("of Ardougne from threats,", 65),
                    ),
                    Page(
                        BookLine("spotting them before they", 66),
                        BookLine("had the chance to reach the", 67),
                        BookLine("city. There followed a long", 68),
                        BookLine("period without war during", 69),
                        BookLine("which the outpost was", 70),
                        BookLine("abandoned as a pointless", 71),
                        BookLine("project. Various people", 72),
                        BookLine("visited the outpost for", 73),
                        BookLine("various reasons, but none", 74),
                        BookLine("left any particular mark", 75),
                        BookLine("on history. The first and", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("most important mark wasn't", 55),
                        BookLine("until 65 years after the", 56),
                        BookLine("establishment of the outpost,", 57),
                        BookLine("when a group of Zamorak", 58),
                        BookLine("followers moved in.", 59),
                        BookLine("", 60),
                        BookLine("The Zamorakians' only purpose", 61),
                        BookLine("seemed to be the disruption", 62),
                        BookLine("of the nearby city of Ardougne,", 63),
                        BookLine("over the years now known as", 64),
                        BookLine("the 'The Dreaded", 65),
                    ),
                    Page(
                        BookLine("Years of Tragedy'.", 66),
                        BookLine("The leader of the Zamorak", 67),
                        BookLine("followers had once had a", 68),
                        BookLine("great friend. However, when", 69),
                        BookLine("they were still young they", 70),
                        BookLine("had both fallen apart in a", 71),
                        BookLine("disagreement over religion.", 72),
                        BookLine("One decided that Saradomin", 73),
                        BookLine("was the god for him, and the", 74),
                        BookLine("other chose Zamorak. It just", 75),
                        BookLine("so happened that when the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("City of Ardougne called for", 55),
                        BookLine("help to rid them of these", 56),
                        BookLine("Zamorakians, that the incoming", 57),
                        BookLine("force were Saradominists", 58),
                        BookLine("led by the other ex-friend.", 59),
                        BookLine("This in-turn led to a battle", 60),
                        BookLine("simply known as", 61),
                        BookLine("'The Great Battle',", 62),
                        BookLine("perhaps because little was known", 63),
                        BookLine("about its activity with it being", 64),
                        BookLine("a small battle in terms of", 65),
                    ),
                    Page(
                        BookLine("numbers, (although", 66),
                        BookLine("not in power, we suspect).", 67),
                        BookLine("The battle produced two", 68),
                        BookLine("survivors - the two ex-friends,", 69),
                        BookLine("both being the most powerful", 70),
                        BookLine("mages. It is said that they", 71),
                        BookLine("settled their differences on", 72),
                        BookLine("the grounds of the friendship", 73),
                        BookLine("they had lost, as well as the", 74),
                        BookLine("loss of life and time.", 75),
                        BookLine("Settling for the neutral god", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Guthix, the two went about", 55),
                        BookLine("their ways to spread equality ", 56),
                        BookLine("mong the people. One of the", 57),
                        BookLine("friends became king, to listen", 58),
                        BookLine("to people's views and ensure", 59),
                        BookLine("a fair and equal life for", 60),
                        BookLine("everyone, whilst the other", 61),
                        BookLine("friend founded the market place,", 62),
                        BookLine("allowing people to trade their", 63),
                        BookLine("skills and wares under equal", 64),
                        BookLine("rights and opportunities.", 65),
                    ),
                ),
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

        fun openBook(player: Player) {
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, Companion::display)
        }
    }
}
