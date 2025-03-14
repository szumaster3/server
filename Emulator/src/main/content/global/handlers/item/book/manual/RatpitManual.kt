package content.global.handlers.item.book.manual

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class RatpitManual : InteractionListener {
    companion object {
        private const val TITLE = "The Ratpits Manual"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Disclaimer/Warning:", 55),
                        BookLine("Rat pit fighting is", 56),
                        BookLine("dangerous for the ill", 57),
                        BookLine("prepared. Lots of cats", 58),
                        BookLine("have died taking part in", 59),
                        BookLine("the past and many more", 60),
                        BookLine("will surely die in the", 61),
                        BookLine("future. This needless", 62),
                        BookLine("death can be avoided", 63),
                        BookLine("completely by adopting", 64),
                        BookLine("the correct precautions.", 65),
                    ),
                    Page(
                        BookLine("Objective:", 66),
                        BookLine("The aim of rat pit", 67),
                        BookLine("fighting is to train your", 68),
                        BookLine("cat in a fun and", 69),
                        BookLine("profitable manner. Rules", 70),
                        BookLine("vary from pit to pit.", 71),
                        BookLine("", 72),
                        BookLine("Each pit allows a certain", 73),
                        BookLine("type of cat fight there", 74),
                        BookLine("e.g. the Ardougne pit", 75),
                        BookLine("only permits kittens to", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("fight in it.", 55),
                    ),
                    Page(
                        BookLine("How to challenge:", 66),
                        BookLine("To compete you must", 67),
                        BookLine("have the correct type of", 68),
                        BookLine("cat for the pit and have", 69),
                        BookLine("some spare change. If", 70),
                        BookLine("you have both of these,", 71),
                        BookLine("you can then challenge", 72),
                        BookLine("another cat trainer.", 73),
                        BookLine("", 74),
                        BookLine("The player you challenge", 75),
                        BookLine("must also possess the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("correct type of cat and", 55),
                        BookLine("some money.", 56),
                        BookLine("", 57),
                        BookLine("Before you proceed to", 58),
                        BookLine("the bidding stage tactics", 58),
                        BookLine("should be discussed with", 59),
                        BookLine("your cat.", 60),
                    ),
                    Page(
                        BookLine("", 69),
                        BookLine("Tactics:", 70),
                        BookLine("A player should tell", 71),
                        BookLine("their cat what sort of strategy", 72),
                        BookLine("they should adopt. To do", 73),
                        BookLine("this competitors should", 74),
                        BookLine("wear an amulet of", 75),
                        BookLine("catspeak when initiating a", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("a fight and accepting a", 55),
                        BookLine("challenge.", 56),
                        BookLine("", 57),
                        BookLine("Cautious tactics should be", 58),
                        BookLine("adopted by those who are", 59),
                        BookLine("unwilling to part with", 60),
                        BookLine("their cat.", 61),
                        BookLine("", 62),
                        BookLine("Using these tactics will aid", 63),
                        BookLine("the cat in terms of", 64),
                        BookLine("increased defence", 65),
                    ),
                    Page(
                        BookLine("bonuses but penalize them", 66),
                        BookLine("as their cats will get out", 67),
                        BookLine("of the pits faster rather", 68),
                        BookLine("than risking death.", 69),
                        BookLine("", 70),
                        BookLine("Using no tactics or", 70),
                        BookLine("aggressive tactics will", 71),
                        BookLine("result in your cat", 72),
                        BookLine("fighting to the death.", 73),
                        BookLine("Aggressive tactics increase", 74),
                        BookLine("your cat's attack levels.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Bidding:", 55),
                        BookLine("Once a challenge has", 56),
                        BookLine("been accepted and both", 57),
                        BookLine("cats advised on tactics the", 58),
                        BookLine("bidding takes place.", 59),
                        BookLine("", 60),
                        BookLine("Both owners can place a", 61),
                        BookLine("small wager on the", 62),
                        BookLine("outcome of the match.", 63),
                        BookLine("The wager must be the", 64),
                        BookLine("same for both sides.", 65),
                    ),
                    Page(
                        BookLine("Winning and losing", 66),
                        BookLine("Rules differ for each pit,", 67),
                        BookLine("in the kitten pits of", 68),
                        BookLine("Ardougne the first cat to", 69),
                        BookLine("kill 5 rats is declared the", 70),
                        BookLine("winner, this number", 71),
                        BookLine("varies throughout other", 72),
                        BookLine("pits.", 73),
                        BookLine("", 74),
                        BookLine("If your cat dies during a", 75),
                        BookLine("a fight or runs away your", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("your opponent is declared the", 55),
                        BookLine("winner.", 56),
                        BookLine("", 57),
                        BookLine("Cats tire quickly and", 58),
                        BookLine("after a certain period of", 59),
                        BookLine("time become too", 60),
                        BookLine("exhausted to continue", 61),
                        BookLine("fighting, in this case the", 62),
                        BookLine("cat with the largest", 63),
                        BookLine("number of kills is", 64),
                        BookLine("declared the winner.", 65),
                    ),
                    Page(
                        BookLine("If both cats have killed", 67),
                        BookLine("the same number of rats,", 68),
                        BookLine("the match is declared a", 69),
                        BookLine("draw.", 70),
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
        on(Items.BOOK_6767, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
