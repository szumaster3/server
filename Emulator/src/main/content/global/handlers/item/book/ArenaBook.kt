package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class ArenaBook : InteractionListener {
    companion object {
        private const val TITLE = "The Arena's History"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("The Arena's History As", 55),
                        BookLine("the manufacture of runestones", 56),
                        BookLine("intensified at the start", 57),
                        BookLine("of the Fifth Age, the", 58),
                        BookLine("magic arts became available", 59),
                        BookLine("to people of a great varying", 60),
                        BookLine("age and background. It", 61),
                        BookLine("soon became evident just", 62),
                        BookLine("how dangerous this was,", 63),
                        BookLine("with a great many tragic", 64),
                        BookLine("accidents occurring due", 65),
                    ),
                    Page(
                        BookLine("to inexperienced wizards.", 66),
                        BookLine("Wizards and victims alike", 67),
                        BookLine("were calling for something", 68),
                        BookLine("to be done, but it was", 69),
                        BookLine("only due to a tragic accident", 70),
                        BookLine("involving one of the leaders", 71),
                        BookLine("of that time that the", 72),
                        BookLine("training arena was", 73),
                        BookLine("constructed.", 74),
                        BookLine("Established with all skill", 75),
                        BookLine("levels in mind, it was", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("used for a great many", 55),
                        BookLine("years until it was destroyed", 56),
                        BookLine("due to conflict of opinion.", 57),
                        BookLine("There were many warriors", 58),
                        BookLine("of melee and ranged combat", 59),
                        BookLine("that took offence to this", 60),
                        BookLine("new art of magic, and", 61),
                        BookLine("taking matters into their", 62),
                        BookLine("own hands, they destroyed", 63),
                        BookLine("the arena. At the time", 64),
                        BookLine("of the writing of this", 65),
                    ),
                    Page(
                        BookLine("book, a collection of", 66),
                        BookLine("Wizards took it upon", 67),
                        BookLine("themselves to", 68),
                        BookLine("resurrect the building", 69),
                        BookLine("along with all Guardians", 70),
                        BookLine("of Magic that were destroyed", 71),
                        BookLine("with it. These Guardians", 72),
                        BookLine("were created out of the", 73),
                        BookLine("very same essence as", 74),
                        BookLine("runestones,", 75),
                        BookLine("embodying the magic power", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("and authority needed to", 55),
                        BookLine("oversee the arena.", 56),
                        BookLine("2) The Pizazz Points", 57),
                        BookLine("System The arena", 58),
                        BookLine("wants to reward mages", 59),
                        BookLine("for their perseverance", 60),
                        BookLine("in such a complex skill.", 61),
                        BookLine("Four types of Pizazz Points", 62),
                        BookLine("can be earn from the", 63),
                        BookLine("four areas in the arena.", 64),
                        BookLine("These are: Telekinetic", 65),
                    ),
                    Page(
                        BookLine("Pizazz Points Enchantment", 66),
                        BookLine("Pizazz Points Alchemist", 67),
                        BookLine("Pizazz Points Graveyard", 68),
                        BookLine("Pizazz Points By earning", 69),
                        BookLine("points for performing", 70),
                        BookLine("the tasks in the four", 71),
                        BookLine("areas, a mage can prove", 72),
                        BookLine("his adeptness at the", 73),
                        BookLine("different skills,", 74),
                        BookLine("and claim items to", 75),
                        BookLine("display his skill.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("In order to keep track", 55),
                        BookLine("of the mage's progress,", 56),
                        BookLine("a special type of hat", 57),
                        BookLine("was commissioned. Carrying", 58),
                        BookLine("this chap around, and", 59),
                        BookLine("by talking to it nicely,", 60),
                        BookLine("he will keep track of", 61),
                        BookLine("a mage's Pizazz Points", 62),
                        BookLine("for future record. The", 63),
                        BookLine("best way to please the", 64),
                        BookLine("hat is by collecting points,", 65),
                    ),
                    Page(
                        BookLine("by which many astute students", 66),
                        BookLine("have noticed the visual", 67),
                        BookLine("change in the hat. However", 68),
                        BookLine("there is a limit to the", 69),
                        BookLine("total number of points", 70),
                        BookLine("that can be collected", 71),
                        BookLine("without spending them,", 72),
                        BookLine("but this won't stop mages", 73),
                        BookLine("from continuing in their", 74),
                        BookLine("training.", 75),
                        BookLine("3) Telekinetic", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Theater The ability to", 55),
                        BookLine("use magic to sense the", 56),
                        BookLine("existence of objects and", 57),
                        BookLine("then move them has a great", 58),
                        BookLine("many purposes in day-to-day", 59),
                        BookLine("life. This theater employs", 60),
                        BookLine("the mage to use their", 61),
                        BookLine("telekinetic spell to move", 62),
                        BookLine("a statue around a maze.", 63),
                        BookLine("Standing on the side they", 64),
                        BookLine("wish to move the statue", 65),
                    ),
                    Page(
                        BookLine("towards, the goal is to", 66),
                        BookLine("get the statue to the", 67),
                        BookLine("solution square. This", 68),
                        BookLine("also improves the awareness", 69),
                        BookLine("of the mage assessing", 70),
                        BookLine("the consequences of casting", 71),
                        BookLine("the spell: just how will", 72),
                        BookLine("the object move? Once", 73),
                        BookLine("the maze has been solved,", 74),
                        BookLine("the statue will be brought", 75),
                        BookLine("to life, providing the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("option to move onto the", 55),
                        BookLine("next maze. Reward is given", 56),
                        BookLine("in the form of 2 Telekinetic", 57),
                        BookLine("Pizazz points for each", 58),
                        BookLine("maze solved, and a bonus", 59),
                        BookLine("for every five mazes solved", 60),
                        BookLine("in a row.", 61),
                        BookLine("4) Alchemists'", 62),
                        BookLine("Playground The arena", 63),
                        BookLine("is free to all those that", 64),
                        BookLine("wish to train here, but", 65),
                    ),
                    Page(
                        BookLine("it costs to maintain the", 66),
                        BookLine("building, and the Guardians", 67),
                        BookLine("don't work for free! The", 68),
                        BookLine("popularity of the alchemy", 69),
                        BookLine("spell has become apparent", 70),
                        BookLine("over the years, and so", 71),
                        BookLine("in this playground of", 72),
                        BookLine("sorts, the art of alchemy", 73),
                        BookLine("can be practiced by finding", 74),
                        BookLine("the most expensive items", 75),
                        BookLine("in the playground and", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("converting them into coins.", 55),
                        BookLine("The money must be deposited", 56),
                        BookLine("in the receptacle opposite", 57),
                        BookLine("the entrance before leaving,", 58),
                        BookLine("otherwise it is taken", 59),
                        BookLine("upon leaving the playground", 60),
                        BookLine("without reward. Although,", 61),
                        BookLine("we would rather trust", 62),
                        BookLine("the mages, some are just", 63),
                        BookLine("far too greedy and deceitful!", 64),
                        BookLine("Every 100 coins will reward", 65),
                    ),
                    Page(
                        BookLine("the mage with one Alchemy", 66),
                        BookLine("Pizazz Point, and you'll", 67),
                        BookLine("also get 10% of your coins", 68),
                        BookLine("back as an added thank", 69),
                        BookLine("you.", 70),
                        BookLine("5) Enchanting Chamber When", 71),
                        BookLine("the wizards first created", 72),
                        BookLine("the arena, they created", 73),
                        BookLine("special artefacts that", 74),
                        BookLine("would change shape and", 75),
                        BookLine("react to the attempt to", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("enchant them. These artefacts", 55),
                        BookLine("could store the power", 56),
                        BookLine("from the spell and be", 57),
                        BookLine("used to drive magical", 58),
                        BookLine("devices. Hence in this", 59),
                        BookLine("arena, enchanting the", 60),
                        BookLine("various shapes, will create", 61),
                        BookLine("orbs that can be deposited", 62),
                        BookLine("to help give power back", 63),
                        BookLine("to the arena itself. For", 64),
                        BookLine("every ten shapes enchanted,", 65),
                    ),
                    Page(
                        BookLine("Pizazz Points are rewarded", 66),
                        BookLine("depending on the level", 67),
                        BookLine("of enchantment spell used,", 68),
                        BookLine("and for every 20 orbs", 69),
                        BookLine("given, an item of magic", 70),
                        BookLine("equipment is awarded.", 71),
                        BookLine("An extra Pizazz Point", 72),
                        BookLine("is also given for every", 73),
                        BookLine("shape enchanted as indicated", 74),
                        BookLine("by the Enchantment guardian", 75),
                        BookLine("on hand. It's also a good", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("idea to watch out for", 55),
                        BookLine("rare dragon stones which", 56),
                        BookLine("share similar properties", 57),
                        BookLine("as the shapes, but can", 58),
                        BookLine("be converted for extra", 59),
                        BookLine("Pizazz Points. 6) Creature", 60),
                        BookLine("Graveyard A lot of death", 61),
                        BookLine("occurs around RuneScape", 62),
                        BookLine("leaving remains lying", 63),
                        BookLine("around for all to see.", 64),
                        BookLine("The wizards took it upon", 65),
                    ),
                    Page(
                        BookLine("themselves to tidy up", 66),
                        BookLine("this mess and put the", 67),
                        BookLine("bones gathered to a better", 68),
                        BookLine("use. Teleporting the bones", 69),
                        BookLine("into this graveyard, the", 70),
                        BookLine("Bones to Bananas spell", 71),
                        BookLine("can be used to create", 72),
                        BookLine("food which can be delivered", 73),
                        BookLine("to hungry beasts around", 74),
                        BookLine("RuneScape. There are different", 75),
                        BookLine("types, which will reward", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("you with, 1, 2, 3, or", 55),
                        BookLine("4 bananas as thus:", 56),
                        BookLine("2 Gives 1 Banana", 57),
                        BookLine("Gives 2 Bananas", 58),
                        BookLine("3 Banana Gives", 59),
                        BookLine("4 Bananas Different", 60),
                        BookLine("piles will yield different", 61),
                        BookLine("bones, changing type as", 62),
                        BookLine("people take from them.", 63),
                        BookLine("Mages can even practice", 64),
                        BookLine("to earn the right to learn", 65),
                    ),
                    Page(
                        BookLine("a new spell - the Bones", 66),
                        BookLine("to Peaches spell, which", 67),
                        BookLine("can be bought from the", 68),
                        BookLine("Rewards Guardian above", 69),
                        BookLine("the entrance hall. A great", 70),
                        BookLine("many runestones are also", 71),
                        BookLine("found with the remains", 72),
                        BookLine("of bodies, and the wizards", 73),
                        BookLine("have gathered those as", 74),
                        BookLine("a reward for supplying", 75),
                        BookLine("food. There is a downside", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to the graveyard however:", 55),
                        BookLine("the bones are often", 56),
                        BookLine("teleported right on-top", 57),
                        BookLine("of the mages,", 58),
                        BookLine("and so the food may come", 59),
                        BookLine("in handy to recover from", 60),
                        BookLine("any damage received!", 61),
                        BookLine("Good luck!", 62),
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
        on(Items.ARENA_BOOK_6891, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
