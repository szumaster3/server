package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class GameBook : InteractionListener {
    /*
     * It features guides to all the games the player, can
     * make and play at poh, for Games or Combat Room.
     */

    companion object {
        private const val TITLE = "Party Pete's Bumper Book of Games"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("HANGMAN", 97),
                        BookLine("", 68),
                        BookLine("This word-guessing game was", 69),
                        BookLine("invented by Mad King", 70),
                        BookLine("Narras of Ardougne in order", 71),
                        BookLine("to encourage literacy among", 72),
                        BookLine("his subjects. Today it is", 73),
                        BookLine("played with an effigy rather", 74),
                        BookLine("than a real person, and it", 75),
                        BookLine("makes an amusing distraction", 76),
                        BookLine("for one or more players.", 77),
                        BookLine("", 78),
                        BookLine("When the game is started,", 79),
                        BookLine("the hangman game chooses a", 80),
                        BookLine("word at random. Players", 81),
                    ),
                    Page(
                        BookLine("take turns to guess one", 82),
                        BookLine("letter at a time. If they", 83),
                        BookLine("guess correctly, that letter is", 84),
                        BookLine("revealed, but if they are", 85),
                        BookLine("incorrect then the scaffold", 86),
                        BookLine("and effigy are built one step", 87),
                        BookLine("at a time until the effigy is", 88),
                        BookLine("hanged. Players can also", 89),
                        BookLine("guess multiple letters if they", 90),
                        BookLine("think they know the word,", 91),
                        BookLine("but if they guess incorrectly", 92),
                        BookLine("no letters will be revealed.", 93),
                        BookLine("The player to complete", 94),
                        BookLine("the word wins.", 95),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("FAIRY TREASURE", 97),
                        BookLine("HUNT", 68),
                        BookLine("", 69),
                        BookLine("The Treasure Fairies delight", 70),
                        BookLine("in hiding in human dwellings", 71),
                        BookLine("and watching in glee as", 72),
                        BookLine("people try to find them.", 73),
                        BookLine("While once this caused havoc", 74),
                        BookLine("throughout Misthalin, since", 75),
                        BookLine("then the fairies have turned", 76),
                        BookLine("this activity into a fun party", 77),
                        BookLine("game.", 78),
                        BookLine("", 79),
                        BookLine("When summoned from her", 80),
                        BookLine("mushroom house, the fairy", 81),
                    ),
                    Page(
                        BookLine("will hide somewhere in the", 82),
                        BookLine("house or its grounds. Each", 83),
                        BookLine("player is given a magic stone", 84),
                        BookLine("that will tell them how close", 85),
                        BookLine("they are to the fairy. The", 86),
                        BookLine("first player to use the stone", 87),
                        BookLine("while standing at the right", 88),
                        BookLine("place wins.", 89),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("MIMIC-THE-JESTER", 97),
                        BookLine("", 68),
                        BookLine("The jester is a cousin of the", 69),
                        BookLine("mime, but rather than", 70),
                        BookLine("summoning people from their", 71),
                        BookLine("activities to pass its test, the", 72),
                        BookLine("jester is happy to be", 73),
                        BookLine("summoned whenever a game", 74),
                        BookLine("is desired. It will perform", 75),
                        BookLine("gestures which the players", 76),
                        BookLine("must try to copy. The first", 77),
                        BookLine("player to get ten correct", 78),
                        BookLine("wins!", 79),
                    ),
                    Page(
                        BookLine("ELEMENTAL BALANCE", 82),
                        BookLine("", 83),
                        BookLine("The Elemental Balance is a", 84),
                        BookLine("battle of wits and wizardry.", 85),
                        BookLine("When the sphere is summoned", 86),
                        BookLine("it is unbalanced on the two", 87),
                        BookLine("elemental axes, earth-air", 88),
                        BookLine("and fire-water. The direction", 89),
                        BookLine("in which it is most unbalanced", 90),
                        BookLine("can be seen by its colour.", 91),
                        BookLine("", 92),
                        BookLine("To balance the sphere, players", 93),
                        BookLine("must attack it with spells of", 94),
                        BookLine("the opposite element. (So if", 95),
                        BookLine("it is red, players must use", 96),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("water spells.) The winner is", 97),
                        BookLine("the player who casts the spell", 68),
                        BookLine("that puts it in perfect", 69),
                        BookLine("balance.", 70),
                    ),
                    Page(
                        BookLine("ATTACK STONE", 82),
                        BookLine("This ancient game was once", 84),
                        BookLine("used as a means of resolving", 85),
                        BookLine("leadership disputes.", 86),
                        BookLine("", 87),
                        BookLine("The Attack Stone is simply a", 88),
                        BookLine("pillar of clay, limestone or", 89),
                        BookLine("marble, carefully constructed", 90),
                        BookLine("to respond differently to", 91),
                        BookLine("stabbing, slashing and", 92),
                        BookLine("crushing. If it takes enough", 93),
                        BookLine("damage in any of these three", 94),
                        BookLine("forms it shatters, and the", 95),
                        BookLine("player who struck that blow", 96),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("wins.", 97),
                    ),
                    Page(
                        BookLine("RANGING GAMES", 82),
                        BookLine("The stick-and-hoop, dartboard", 84),
                        BookLine("and archery game are all", 85),
                        BookLine("played using the same rules.", 86),
                        BookLine("Players take turns to throw", 87),
                        BookLine("or shoot at the target in", 88),
                        BookLine("order to score points. The", 89),
                        BookLine("range of points that can be", 90),
                        BookLine("awarded depends on the", 91),
                        BookLine("target: with the hoop there is", 92),
                        BookLine("only hitting and missing, with", 93),
                        BookLine("the dartboard the maximum", 94),
                        BookLine("is 3 and with the archery", 95),
                        BookLine("target it is 10. After all", 96),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("players have had ten shots,", 97),
                        BookLine("the highest scoring player", 68),
                        BookLine("wins.", 69),
                    ),
                    Page(
                        BookLine("COMBAT", 82),
                        BookLine("The various combat rings", 84),
                        BookLine("have different restrictions on", 85),
                        BookLine("the weapons that can be used", 86),
                        BookLine("in them. No equipment", 87),
                        BookLine("can be worn in the boxing ring", 88),
                        BookLine("except for boxing gloves", 89),
                        BookLine("(although boxers may fight", 90),
                        BookLine("bare-fisted if they wish). Any", 91),
                        BookLine("weapons can be used in the", 92),
                        BookLine("fencing ring, but no armour", 93),
                        BookLine("of any kind can be worn.", 94),
                        BookLine("There are no restrictions in", 95),
                        BookLine("the combat ring. There are", 96),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("similarly no restrictions for", 97),
                        BookLine("the ranging pedestals, but", 68),
                        BookLine("since the combatants are", 69),
                        BookLine("separated only ranged weapons", 70),
                        BookLine("will be effective! The balance", 71),
                        BookLine("beam allows only one weapon,", 72),
                        BookLine("the pugel stick, and fighting", 73),
                        BookLine("on the beam is a test of", 74),
                        BookLine("agility as well as combat!", 75),
                        BookLine("", 76),
                        BookLine("The combat rings are", 77),
                        BookLine("enchanted so that players", 78),
                        BookLine("cannot kill one another; the", 79),
                        BookLine("loser is simply ejected from", 80),
                        BookLine("the ring and healed.", 81),
                    ),
                    Page(
                        BookLine("PRIZES", 82),
                        BookLine("If the games room has a", 84),
                        BookLine("prize chest, and the house", 85),
                        BookLine("owner has put money in it,", 86),
                        BookLine("then the winner of any of", 87),
                        BookLine("these games will be given a", 88),
                        BookLine("key to the chest so that they", 89),
                        BookLine("can claim their prize!", 90),
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
        on(Items.GAME_BOOK_7681, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
