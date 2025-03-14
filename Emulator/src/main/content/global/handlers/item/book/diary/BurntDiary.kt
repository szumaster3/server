package content.global.handlers.item.book.diary

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.DARK_RED
import org.rs.consts.Items

class BurntDiary : InteractionListener {
    /*
     * Royal Trouble quest Player find the pages of
     * the book in the remains of fires in the tunnels beneath Miscellania
     * and Etceteria during the quest. Players put them together to make this book.
     * The item's examine text also seems to change to reflect how many of the five
     * pages the player has found so far.
     */

    companion object {
        private const val TITLE = "Armod's Burnt Diary"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Year 5 of trials, day 78", 55),
                        BookLine("", 56),
                        BookLine("It's not fair! Father said", 57),
                        BookLine("that me and my friends", 58),
                        BookLine("might never pass our", 59),
                        BookLine("trials. They dont want", 60),
                        BookLine("us helping other people", 61),
                        BookLine("with their trials, either.", 62),
                        BookLine("And now he says we have", 63),
                        BookLine("to leave Rellekka! How", 64),
                        BookLine("can they exile us?", 65),
                    ),
                    Page(
                        BookLine("Year 5 of trials, day 89", 66),
                        BookLine("", 67),
                        BookLine("Signy said she heard", 68),
                        BookLine("about two island kingdoms", 69),
                        BookLine("that had been at war for", 70),
                        BookLine("hundreds of years. If we", 71),
                        BookLine("go there and make peace,", 72),
                        BookLine("will my father finally", 73),
                        BookLine("agree that we're true", 74),
                        BookLine("Fremennik adults?", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Year 5 of trials, day 93", 55),
                        BookLine("", 56),
                        BookLine("Life's not fair!", 57),
                        BookLine("We got to the island", 58),
                        BookLine("kingdoms and they said", 59),
                        BookLine("that an adventurer had", 60),
                        BookLine("recently made peace, by", 61),
                        BookLine("promising to marry the", 62),
                        BookLine("son of the king!", 63),
                        BookLine("Why does this always", 64),
                        BookLine("happen to us? If we'd", 65),
                    ),
                    Page(
                        BookLine("just been a little earlier, I", 66),
                        BookLine("could have married the", 67),
                        BookLine("king's daughter.", 68),
                        BookLine("", 69),
                        BookLine("Year 5 of trials, day 95", 70),
                        BookLine("", 71),
                        BookLine("Beigarth says that just", 72),
                        BookLine("because they're at peace", 73),
                        BookLine("now doesn't mean they", 74),
                        BookLine("always will. He says he", 75),
                        BookLine("has a plan.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("Year 5 of trials, day 95", 56),
                        BookLine("", 57),
                        BookLine("Beigarth's plan is great!", 58),
                        BookLine("Now all we have to do is", 59),
                        BookLine("find the right armour,", 60),
                        BookLine("and a place in one of the", 61),
                        BookLine("caves. Father's going to", 62),
                        BookLine("be so impressed with me...", 63),
                    ),
                    Page(
                        BookLine("Year 5 of trials, day 98", 66),
                        BookLine("", 67),
                        BookLine("The plan's worked! The", 68),
                        BookLine("kingdoms are at war", 69),
                        BookLine("again, and no-one knows", 70),
                        BookLine("we were those soldiers. All", 71),
                        BookLine("we need to do is make", 72),
                        BookLine("peace, and the council at", 73),
                        BookLine("home will be so impressed", 74),
                        BookLine("that they'll make us adults", 75),
                        BookLine("right away!", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("Year 5 of trials, day 99", 56),
                        BookLine("", 57),
                        BookLine("Why did it go so wrong?", 58),
                        BookLine("We were just about to", 59),
                        BookLine("get the goods back, but", 60),
                        BookLine("there are MONSTERS", 61),
                        BookLine("in there! I'm not going", 62),
                        BookLine("anywhere near that giant", 63),
                        BookLine("snake, and I think", 64),
                        BookLine("the others agree with me.", 65),
                    ),
                    Page(
                        BookLine("We're cold and hungry", 66),
                        BookLine("and can't go back to the", 67),
                        BookLine("town, because we got", 68),
                        BookLine("drunk in the inn and", 69),
                        BookLine("then couldn't pay. It's so", 70),
                        BookLine("bad that we've had to", 71),
                        BookLine("burn bits of my diary to keep warm.", 72),
                        BookLine("Someone please help us...", 73),
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
        on(Items.BURNT_DIARY_7965, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
