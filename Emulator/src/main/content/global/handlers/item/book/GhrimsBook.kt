package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class GhrimsBook : InteractionListener {
    /*
     * Obtainable during the Throne of Miscellania quest.
     */

    companion object {
        private const val TITLE = "Managing Thine Kingdom"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("- for Noobes by A. Ghrim", 55),
                        BookLine("<col=08088A>2nd Edition</col>", 56),
                        BookLine("Wherein is contained the", 58),
                        BookLine("TRUE and TESTED", 59),
                        BookLine("knowledge of the AUTHOR", 60),
                        BookLine("in matters pertaining to", 61),
                        BookLine("the RUNNING of one's", 62),
                        BookLine("KINGDOM. Collecting", 63),
                        BookLine("consts Your loyal", 64),
                        BookLine("subjects will work hard", 65),
                    ),
                    Page(
                        BookLine("to gather consts", 66),
                        BookLine("for you. When you wish", 67),
                        BookLine("to collect what has", 68),
                        BookLine("been gathered, talk", 69),
                        BookLine("to your Advisor so that", 70),
                        BookLine("they may deliver them", 71),
                        BookLine("to your bank. Remember", 72),
                        BookLine("to be patient! Rather", 73),
                        BookLine("than continually pestering", 74),
                        BookLine("your Advisor to check", 75),
                        BookLine("if more consts have", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("been brought in yet", 55),
                        BookLine("consider what methods", 56),
                        BookLine("you can use to increase", 57),
                        BookLine("the amounts collected,", 58),
                        BookLine("which are set forth", 59),
                        BookLine("in the pages that follow.", 60),
                        BookLine("Approval Ratings. Your", 61),
                        BookLine("approval rating has an", 62),
                        BookLine("important effect on the", 63),
                        BookLine("amount of consts gathered", 64),
                        BookLine("by your subjects; the", 65),
                    ),
                    Page(
                        BookLine("more they love you,", 66),
                        BookLine("the more they will collect.", 67),
                        BookLine("If, Guthix forbid, your", 68),
                        BookLine("approval rating should", 69),
                        BookLine("ever drop to near 0,", 70),
                        BookLine("your subjects would", 71),
                        BookLine("be loath to gather anything", 72),
                        BookLine("at all! Your approval", 73),
                        BookLine("rating will drop quite", 74),
                        BookLine("low over time if you", 75),
                        BookLine("do nothing to increase", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("it. The surest way to", 55),
                        BookLine("improve your rating", 56),
                        BookLine("is to go and help your", 57),
                        BookLine("subjects with their", 58),
                        BookLine("resource-gathering tasks. Even", 59),
                        BookLine("helping a single fisherman", 60),
                        BookLine("fish can be enough,", 61),
                        BookLine("for he will surely tell", 62),
                        BookLine("his friends and family", 63),
                        BookLine("how kind you were. The", 64),
                        BookLine("Royal Coffers Your subjects", 65),
                    ),
                    Page(
                        BookLine("are rewarded for their", 66),
                        BookLine("work straight out of", 67),
                        BookLine("the contents of the", 68),
                        BookLine("royal coffers; should", 69),
                        BookLine("there ever be no money", 70),
                        BookLine("remaining in the coffers,", 71),
                        BookLine("your subjects will have", 72),
                        BookLine("to concentrate on satisfying", 73),
                        BookLine("their own needs and", 74),
                        BookLine("so will not be able", 75),
                        BookLine("to gather anything for", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("you. Each day, one", 55),
                        BookLine("tenth of the contents", 56),
                        BookLine("of the royal coffers", 57),
                        BookLine("are distributed to your", 58),
                        BookLine("subjects, up to a maximum", 59),
                        BookLine("of 75,000 coins. Of", 60),
                        BookLine("course, the greater", 61),
                        BookLine("this share, the more", 62),
                        BookLine("they will be eager to", 63),
                        BookLine("work for you, but be", 64),
                        BookLine("careful not to spend", 65),
                    ),
                    Page(
                        BookLine("your riches too frivolously.", 66),
                        BookLine("The coffers will hold", 67),
                        BookLine("up to 7.5 million coins.", 68),
                        BookLine("Division of Labour You", 69),
                        BookLine("may decide at one time", 70),
                        BookLine("or another that you", 71),
                        BookLine("require large amounts", 72),
                        BookLine("of a certain resource", 73),
                        BookLine("above all others;", 74),
                        BookLine("or simply that you do", 75),
                        BookLine("not require any of a", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("certain resource, and", 55),
                        BookLine("do not wish your subjects", 56),
                        BookLine("to gather any of it.", 57),
                        BookLine("By talking to your advisor,", 58),
                        BookLine("you may decide the amount", 59),
                        BookLine("of time and effort your", 60),
                        BookLine("subjects will devote to", 61),
                        BookLine("gathering each resource.", 62),
                        BookLine("Etceterian Citizens has", 63),
                        BookLine("pledged some of its subjects", 64),
                        BookLine("to gather consts for you.", 65),
                    ),
                    Page(
                        BookLine("If you help them with", 66),
                        BookLine("their tasks, they will", 67),
                        BookLine("be as willing as any", 68),
                        BookLine("loyal Miscellanian to", 69),
                        BookLine("aid you and give you", 70),
                        BookLine("the results of their work.", 71),
                        BookLine("their work.", 72),
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
        on(Items.GHRIMS_BOOK_3901, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
