package content.global.handlers.item.book.diary

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class GrimDiary : InteractionListener {
    /*
     * Obtained during the Halloween event.
     */

    companion object {
        private const val TITLE = "Diary of Death"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("<col=08088A>My Diary by Grim", 55),
                        BookLine("<col=08088A>~12th Bennath", 57),
                        BookLine("I had such a busy", 58),
                        BookLine("day dealing out", 59),
                        BookLine("death today. It's", 60),
                        BookLine("not easy being grim.", 61),
                        BookLine("Realised Alfonse is", 62),
                        BookLine("such a good servant.", 63),
                        BookLine("He seems to have the", 64),
                        BookLine("house in working order.", 65),
                    ),
                    Page(
                        BookLine("I shall have to congratulate", 66),
                        BookLine("him tomorrow. Spent", 67),
                        BookLine("some well-deserved", 68),
                        BookLine("time sharpening my", 69),
                        BookLine("scythe - Alfonse kindly", 70),
                        BookLine("reminded me to put", 71),
                        BookLine("the sharpener back", 72),
                        BookLine("in the cabinet. such", 73),
                        BookLine("a good chap.", 74),
                        BookLine("<col=08088A>~13th Bennath", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Went to Wilderness", 55),
                        BookLine("today. Plenty of foolish", 56),
                        BookLine("people surrendering", 57),
                        BookLine("their lives to me without", 58),
                        BookLine("thought. My back is", 59),
                        BookLine("killing me from all", 60),
                        BookLine("the stand around waiting.", 61),
                        BookLine("Must get that seen", 62),
                        BookLine("to. Got my robes stained", 63),
                        BookLine("from one victim. Simply", 64),
                        BookLine("ruined! I decided to", 65),
                    ),
                    Page(
                        BookLine("throw them in the fireplace", 66),
                        BookLine("- will light the fire", 67),
                        BookLine("soon. Oh and I must", 68),
                        BookLine("remember to call mother.", 69),
                        BookLine("<col=08088A>~14th Bennath", 71),
                        BookLine("A tragic day.", 72),
                        BookLine("Alfonse and I were", 73),
                        BookLine("in the garden looking", 74),
                        BookLine("at the state of a spiders", 75),
                        BookLine("nest. I patted my trusty", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("servant on the back", 55),
                        BookLine("in thanks for all his", 56),
                        BookLine("hard work. Sadly, my", 57),
                        BookLine("touch of death killed", 58),
                        BookLine("him instantly. Feel", 59),
                        BookLine("quite guilty.", 60),
                        BookLine("<col=08088A>~20th Bennath", 62),
                        BookLine("House is getting", 63),
                        BookLine("into quite a state", 64),
                        BookLine("without Alfonse - things", 65),
                    ),
                    Page(
                        BookLine("strewn all over the", 66),
                        BookLine("place. Almost trod", 67),
                        BookLine("on the eye of my mentor,", 68),
                        BookLine("eww. I put the eye", 69),
                        BookLine("back on the shelf so", 70),
                        BookLine("he can watch over me", 71),
                        BookLine("and make sure I stay", 72),
                        BookLine("true to his teachings.", 73),
                        BookLine("Went into Varrock to", 74),
                        BookLine("buy some new robes", 75),
                        BookLine("- people kept running", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("away in fear, so it", 55),
                        BookLine("was quite hard to find", 56),
                        BookLine("a sale.", 57),
                        BookLine("<col=08088A>~21st Bennath", 59),
                        BookLine("Decided to spend a", 60),
                        BookLine("bit of time tidying", 61),
                        BookLine("today - it really", 62),
                        BookLine("isn't an easy", 63),
                        BookLine("job. In my activities", 64),
                        BookLine("I found my 'Voice of", 65),
                    ),
                    Page(
                        BookLine("Doom' potion on my", 66),
                        BookLine("bookcase - perfect", 67),
                        BookLine("for giving people a", 68),
                        BookLine("good scare. Oh I do", 69),
                        BookLine("love my job.", 70),
                        BookLine("<col=08088A>~22nd Bennath", 72),
                        BookLine("Ordered a new servant", 73),
                        BookLine("from the agency", 74),
                        BookLine("today and got a 10%", 75),
                        BookLine("discount for getting", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("past the 1000th", 55),
                        BookLine("servant mark. Woo hoo!", 56),
                        BookLine("This agency has sent", 57),
                        BookLine("me his Last Will and", 58),
                        BookLine("Testament. shall have", 59),
                        BookLine("to sit on that for", 60),
                        BookLine("a while.", 61),
                        BookLine("<col=08088A>~23rd Bennath", 63),
                        BookLine("Acquired some bones", 64),
                        BookLine("today. Muncher should", 65),
                    ),
                    Page(
                        BookLine("appreciate them", 66),
                        BookLine("as a treat the next", 67),
                        BookLine("time he behaves. The", 68),
                        BookLine("problem is there aren't", 69),
                        BookLine("many barriers he can't", 70),
                        BookLine("devour, so I locked", 71),
                        BookLine("them up in the chest.", 72),
                        BookLine("<col=08088A>~24th Bennath", 74),
                        BookLine("My plan to make", 75),
                        BookLine("the undead fish", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("is going quite well.", 55),
                        BookLine("I managed to obtain", 56),
                        BookLine("a resurrection hourglass", 57),
                        BookLine("today, so have added", 58),
                        BookLine("that to the fishtank", 59),
                        BookLine("to finish the process.", 60),
                        BookLine("It's so difficult to", 61),
                        BookLine("have pets when everything", 62),
                        BookLine("you touch dies horrifically.", 63),
                        BookLine("I remember having a", 64),
                        BookLine("rabbit once that exploded", 65),
                    ),
                    Page(
                        BookLine("when I fed it a carrot!", 66),
                        BookLine("<col=08088A>~25th Bennath", 68),
                        BookLine("Got back today", 69),
                        BookLine("to find Muncher has", 70),
                        BookLine("run around the house", 71),
                        BookLine("creating havok - he", 72),
                        BookLine("even ate the postman!", 73),
                        BookLine("I scolded him, so hopefully", 74),
                        BookLine("he won't do it again", 75),
                        BookLine("anytime soon. All my", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("things are in such", 55),
                        BookLine("a mess. I'm surely", 56),
                        BookLine("going to have to find", 57),
                        BookLine("someone to tidy up", 58),
                        BookLine("before my new servant", 59),
                        BookLine("arrives. Don't want", 60),
                        BookLine("to seem totally incapable", 61),
                        BookLine("of looking after myself.", 62),
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
        on(Items.THE_GRIM_REAPERS_DIARY_11780, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}
