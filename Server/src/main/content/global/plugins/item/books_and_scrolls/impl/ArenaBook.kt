package content.global.plugins.item.books_and_scrolls.impl

import content.global.plugins.item.books_and_scrolls.BookInterface
import content.global.plugins.item.books_and_scrolls.BookLine
import content.global.plugins.item.books_and_scrolls.Page
import content.global.plugins.item.books_and_scrolls.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.tools.DARK_BLUE
import core.tools.DARK_RED
import shared.consts.Items

class ArenaBook : InteractionListener {

    /*
     * Comes from the Mage Training Arena. Can be bought from
     * Magic Training Area Shop. It is located on the first
     * floor of MTA, it is run by the Rewards Guardian.
     */

    companion object {
        private const val TITLE = "Magic Training Arena Lore Book"

        private const val ANIMAL_BONES_MODEL_0 = 10569
        private const val ANIMAL_BONES_MODEL_1 = 10570
        private const val ANIMAL_BONES_MODEL_2 = 10571
        private const val ANIMAL_BONES_MODEL_3 = 10572

        private val CONTENTS = arrayOf(
            PageSet(
                Page(
                    BookLine("${DARK_RED}Chapters</col>", 97),
                    BookLine("", 68),
                    BookLine("1) ${DARK_BLUE}The Arena's History</col>", 69),
                    BookLine("2) ${DARK_BLUE}The Pizzaz Points System</col>", 70),
                    BookLine("3) ${DARK_BLUE}The Telekinetic Theatre</col>", 71),
                    BookLine("4) ${DARK_BLUE}Alchemist Playground</col>", 72),
                    BookLine("5) ${DARK_BLUE}Enchanting Chamber</col>", 73),
                    BookLine("6) ${DARK_BLUE}Creature Graveyard</col>", 74),
                ),
                Page(
                    BookLine("1) ${DARK_BLUE}The Arena's History</col>", 82),
                    BookLine("", 83),
                    BookLine("As the manufacture of", 84),
                    BookLine("runestones at the", 85),
                    BookLine("start of the Fifth Age, the", 86),
                    BookLine("magic arts became available to", 87),
                    BookLine("people of a great varying age", 88),
                    BookLine("and background. It soon", 89),
                    BookLine("became evident just how", 90),
                    BookLine("dangerous this was, with a", 91),
                    BookLine("great many tragic accidents", 92),
                    BookLine("occurring due to", 93),
                    BookLine("inexperienced wizards.", 94),
                    BookLine("Wizards and victims alike", 95),
                    BookLine("were calling for something to", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("be done, but it was only due", 97),
                    BookLine("to a tragic accident involving", 68),
                    BookLine("one of the leaders of that", 69),
                    BookLine("time that the training arena", 70),
                    BookLine("was constructed. Established", 71),
                    BookLine("with all skill levels in mind, it", 72),
                    BookLine("was used for a great many", 73),
                    BookLine("years until it was destroyed", 74),
                    BookLine("due to conflict of opinion.", 75),
                    BookLine("There were many warriors", 76),
                    BookLine("of melee and ranged combat", 77),
                    BookLine("that took offence to this new", 78),
                    BookLine("art of magic, and taking", 79),
                    BookLine("matters into their own hands,", 80),
                    BookLine("they destroyed the arena. At", 81),
                ),
                Page(
                    BookLine("the time of the writing of this", 82),
                    BookLine("book, a collection of Wizards", 83),
                    BookLine("took it upon themselves to", 84),
                    BookLine("resurrect the building along", 85),
                    BookLine("with all Guardians of Magic", 86),
                    BookLine("that were destroyed with it.", 87),
                    BookLine("These Guardians were", 88),
                    BookLine("created out of the very same", 89),
                    BookLine("essence as runestones,", 90),
                    BookLine("embodying the magic power", 91),
                    BookLine("and authority needed to", 92),
                    BookLine("oversee the arena.", 93),
                ),
            ),
            PageSet(
                Page(
                    BookLine("2) ${DARK_BLUE}The Pizzaz Points System</col>", 97),
                    BookLine("", 68),
                    BookLine("The arena wants to reward", 69),
                    BookLine("mages for their perseverance", 70),
                    BookLine("in such a complex skill. Four", 71),
                    BookLine("types of Pizazz Points can", 72),
                    BookLine("be earnt from the four areas", 73),
                    BookLine("in the arena. There are:", 74),
                    BookLine("", 75),
                    BookLine("${DARK_BLUE}Telekinetic Pizazz Points</col>", 76),
                    BookLine("${DARK_BLUE}Enchantment Pizazz Points</col>", 77),
                    BookLine("${DARK_BLUE}Alchemist Pizazz Points</col>", 78),
                    BookLine("${DARK_BLUE}Graveyard Pizazz Points</col>", 79),
                    BookLine("", 80),
                    BookLine("By earning points for", 81),
                ),
                Page(
                    BookLine("performing the tasks in the", 82),
                    BookLine("four areas, a mage can prove", 83),
                    BookLine("his adeptness at the different", 84),
                    BookLine("skills, and claim items to", 85),
                    BookLine("display his skill. In order to", 86),
                    BookLine("keep track of the mage's", 87),
                    BookLine("progress, a special type of hat", 88),
                    BookLine("was commissioned. Carrying", 89),
                    BookLine("this chap around, and by", 90),
                    BookLine("talking to it nicely, he will", 91),
                    BookLine("keep track of a mage's", 92),
                    BookLine("Pizazz Points for future", 93),
                    BookLine("record. The best way to", 94),
                    BookLine("please the hat is by collecting", 95),
                    BookLine("points, by which many astute", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("students have noticed the", 97),
                    BookLine("visual change in the hat.", 68),
                    BookLine("However, there is a limit to", 69),
                    BookLine("the total number of points", 70),
                    BookLine("that can be collected without", 71),
                    BookLine("spending them, but this won't", 72),
                    BookLine("stop mages from continuing", 73),
                    BookLine("in their training.", 74),
                ),
                Page(
                    BookLine("3) ${DARK_BLUE}Telekinetic Theatre</col>", 82),
                    BookLine("", 83),
                    BookLine("The ability to use magic to", 84),
                    BookLine("sense the existence of objects", 85),
                    BookLine("and them move them has a", 86),
                    BookLine("great many purposes in day-", 87),
                    BookLine("to-day life. This theatr", 88),
                    BookLine(" employs the mage to use", 89),
                    BookLine(" their telekinetic spell to move", 90),
                    BookLine("a statue around a maze.", 91),
                    BookLine("Standing on the side they", 92),
                    BookLine("wish to move the statue", 93),
                    BookLine("towards, the goal is to get the", 94),
                    BookLine("statue to the solution square.", 95),
                    BookLine("This also improves the", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("awareness of the mage", 97),
                    BookLine("assessing the consequences of", 68),
                    BookLine("casting the spell: just how will", 69),
                    BookLine("the object move? Once the", 70),
                    BookLine("maze has been solved, the", 71),
                    BookLine("statue will be brought to life,", 72),
                    BookLine("providing the option to move", 73),
                    BookLine("onto the next maze. Reward", 74),
                    BookLine("is given in the form of 2", 75),
                    BookLine("Telekinetic Pizazz Points for", 76),
                    BookLine("each maze solved, and a", 77),
                    BookLine("bonus for every five mazes", 78),
                    BookLine("solved in a row.", 79),
                ),
                Page(
                    BookLine("4) ${DARK_BLUE}Alchemists' Playground</col>", 82),
                    BookLine("", 83),
                    BookLine("The arena is free to all those", 84),
                    BookLine("that wish to train here, but it", 85),
                    BookLine("costs to maintain the building,", 86),
                    BookLine("and the Guardians don't", 87),
                    BookLine("work for free! The popularity", 88),
                    BookLine("of the alchemy spell has", 89),
                    BookLine("become apparent over the", 90),
                    BookLine("years, and so in this", 91),
                    BookLine("playground of sorts, the art", 92),
                    BookLine("of alchemy can be practiced", 93),
                    BookLine("by finding the most expensive", 94),
                    BookLine("items in the playground and", 95),
                    BookLine("converting them into coins.", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("The money must be", 97),
                    BookLine("deposited in the receptacle", 68),
                    BookLine("opposite the entrance before", 69),
                    BookLine("leaving, otherwise it is taken", 70),
                    BookLine("upon leaving the playground", 71),
                    BookLine("without reward. Although we", 72),
                    BookLine("would rather trust the mages,", 73),
                    BookLine("some are just far too greedy", 74),
                    BookLine("and deceitful! Every 100", 75),
                    BookLine("coins will reward the mage", 76),
                    BookLine("with one Alchemy Pizazz", 77),
                    BookLine("Point, and you'll also get", 78),
                    BookLine("10% of your coins back as", 79),
                    BookLine("an added thank you.", 80),
                ),
                Page(
                    BookLine("5) ${DARK_BLUE}Enchanting Chamber</col>", 82),
                    BookLine("", 83),
                    BookLine("When the wizard first", 84),
                    BookLine("created the arena, they", 85),
                    BookLine("created special artefacts that", 86),
                    BookLine("would change shape and", 87),
                    BookLine("react to the attempt to", 88),
                    BookLine("enchant them. These", 89),
                    BookLine("artefacts could store the", 90),
                    BookLine("power from the spell and be", 91),
                    BookLine("used to drive magical devices.", 92),
                    BookLine("Hence in this arena,", 93),
                    BookLine("enchanting the various shapes", 94),
                    BookLine("will create orbs that can be", 95),
                    BookLine("deposited to help give power", 96),
                ),
            ),
            PageSet(
                Page(
                    BookLine("back to the arena itself. For", 97),
                    BookLine("every ten shapes enchanted,", 68),
                    BookLine("Pizazz Points are rewarded", 69),
                    BookLine("depending on the level of", 70),
                    BookLine("enchantment spell used, and", 71),
                    BookLine("for every 20 orbs given, an", 72),
                    BookLine("item of mage equipment is", 73),
                    BookLine("awarded. An extra Pizazz", 74),
                    BookLine("Point is also given for every", 75),
                    BookLine("shape enchanted as indicated", 76),
                    BookLine("by the Enchantment", 77),
                    BookLine("Guardian on hand. It's also a", 78),
                    BookLine("good idea to watch out for", 79),
                    BookLine("dragon stones which", 80),
                    BookLine("share similar properties as the", 81),
                ),
                Page(
                    BookLine("shapes, but can be converted", 82),
                    BookLine("for extra Pizazz Points.", 83),
                ),
            ),
            PageSet(
                Page(
                    BookLine("6) ${DARK_BLUE}Creature Graveyard</col>", 97),
                    BookLine("", 68),
                    BookLine("A lot of death occurs around", 69),
                    BookLine("${GameWorld.settings?.name} leaving remains", 70),
                    BookLine("lying around for all to see.", 71),
                    BookLine("The wizards took it upon", 72),
                    BookLine("themselves to tidy up this", 73),
                    BookLine("mess and put the bones", 74),
                    BookLine("gathered to a better use.", 75),
                    BookLine("Teleporting the bones into", 76),
                    BookLine("this graveyard, the Bones to", 77),
                    BookLine("Bananas spell can be used to", 78),
                    BookLine("create food which can be", 79),
                    BookLine("delivered to hungry beasts", 80),
                    BookLine("around ${GameWorld.settings?.name}. There", 81),
                ),
                Page(
                    BookLine("are different types, which will", 82),
                    BookLine("reward you with, 1,2,3 or 4", 83),
                    BookLine("bananas, as thus:", 84),
                    BookLine("", 85),
                    BookLine("", 86),
                    BookLine("", 87),
                    BookLine("", 88), // ANIMAL MODEL 0 (85-91).
                    BookLine("", 89),
                    BookLine("", 90),
                    BookLine("", 91),
                    BookLine("${DARK_BLUE}gives 1 banana,</col>", 92),
                    BookLine("", 93),
                    BookLine("", 94),
                ),
            ),
            PageSet(
                Page(
                    BookLine("", 97),
                    BookLine("", 68),
                    BookLine("", 69), // ANIMAL MODEL 1 (97+68-71).
                    BookLine("", 70),
                    BookLine("", 71),
                    BookLine("${DARK_BLUE}gives 2 bananas,</col>", 72),
                    BookLine("", 73),
                    BookLine("", 74),
                    BookLine("", 75), // ANIMAL MODEL 2 (73-78).
                    BookLine("", 76),
                    BookLine("", 77),
                    BookLine("", 78),
                    BookLine("${DARK_BLUE}gives 3 bananas,</col>", 79),
                    BookLine("", 80),
                    BookLine("", 81),
                ),
                Page(
                    BookLine("", 82),
                    BookLine("", 83), // ANIMAL MODEL 3 (82-86).
                    BookLine("", 84),
                    BookLine("", 85),
                    BookLine("", 86),
                    BookLine("${DARK_BLUE}gives 4 bananas,</col>", 87),
                    BookLine("", 88),
                    BookLine("Different piles will yield", 89),
                    BookLine("different bones, changing", 90),
                    BookLine("type as people take from", 91),
                    BookLine("them. Mages can even", 92),
                    BookLine("practice to earn the right to", 93),
                    BookLine("learn a new spell - the Bones", 94),
                    BookLine("to Peaches spell, which can be", 95),
                ),
            ),
            PageSet(
                Page(
                    BookLine("bought from the Rewards", 97),
                    BookLine("Guardian above the entrance", 68),
                    BookLine("hall. A great many", 69),
                    BookLine("runestones are also found", 70),
                    BookLine("with the remains of bodies,", 71),
                    BookLine("and the wizards have", 72),
                    BookLine("gathered those as a reward", 73),
                    BookLine("for supplying food. There is", 74),
                    BookLine("a downside to the graveyard", 75),
                    BookLine("however: the bones are often", 76),
                    BookLine("teleported right on-top of the", 77),
                    BookLine("mages, and so the food may", 78),
                    BookLine("come in handy to recover", 79),
                    BookLine("from any damage received!", 80),
                ),
                Page(
                    BookLine("${DARK_RED}Good luck!</col>", 82),
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
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_26, TITLE, CONTENTS)
        /*
         * Animal model 0.
         */
        BookInterface.setModelOnPage(
            player,
            7,
            ANIMAL_BONES_MODEL_0,
            BookInterface.FANCY_BOOK_26,
            BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[22],
            BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[22],
            700,
            300,
            0
        )
        /*
         * Animal model 1.
         */
        BookInterface.setModelOnPage(
            player,
            8,
            ANIMAL_BONES_MODEL_1,
            BookInterface.FANCY_BOOK_26,
            BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[2],
            BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[2],
            600,
            300,
            0
        )
        /*
         * Animal model 2.
         */
        BookInterface.setModelOnPage(
            player,
            8,
            ANIMAL_BONES_MODEL_2,
            BookInterface.FANCY_BOOK_26,
            BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[9],
            BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[9],
            600,
            300,
            0
        )
        /*
         * Animal model 3.
         */
        BookInterface.setModelOnPage(
            player,
            8,
            ANIMAL_BONES_MODEL_3,
            BookInterface.FANCY_BOOK_26,
            BookInterface.FANCY_BOOK_26_IMAGE_ENABLE_DRAW_IDS[18],
            BookInterface.FANCY_BOOK_26_IMAGE_DRAW_IDS[18],
            800,
            600,
            0
        )
        return true
    }

    override fun defineListeners() {
        on(Items.ARENA_BOOK_6891, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_26, ::display)
            return@on true
        }
    }
}
