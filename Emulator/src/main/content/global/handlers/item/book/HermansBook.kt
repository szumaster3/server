package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs.consts.Items

class HermansBook : InteractionListener {
    /*
     * Obtainable during the Swan Song quest.
     * Dionysius, also known as the Wise Old Man.
     */

    companion object {
        private const val TITLE = "Dionysius: A Legend in his own Lifetime"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("This world of " + GameWorld.settings!!.name + "", 55),
                        BookLine("has always had its share", 56),
                        BookLine("of heroes throughout its", 57),
                        BookLine("history. Tales of the", 58),
                        BookLine("daring exploits of", 59),
                        BookLine("legendary figures such", 60),
                        BookLine("as Arrav, Camorra,", 61),
                        BookLine("Robert and Wally have", 62),
                        BookLine("held children spellbound", 63),
                        BookLine("for generations. But", 64),
                    ),
                    Page(
                        BookLine("even in these modern", 65),
                        BookLine("times, the world still", 66),
                        BookLine("needs heroes. One such", 67),
                        BookLine("figure,", 68),
                        BookLine("standing protectively", 70),
                        BookLine("between civilisation", 71),
                        BookLine("and the forces of chaos,", 72),
                        BookLine("is that of Dionysius.", 73),
                        BookLine("The young Dionysius was", 75),
                        BookLine("educated in Varrock,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("where records tell us", 55),
                        BookLine("that he regularly", 56),
                        BookLine("attended the local", 57),
                        BookLine("Saradominist chapel.", 58),
                        BookLine("Local tradition has it", 59),
                        BookLine("that Dionysius led a", 60),
                        BookLine("band of young men to", 61),
                        BookLine("graffiti the Zamorakian", 62),
                        BookLine("temple in the south of", 63),
                        BookLine("the city, although", 64),
                        BookLine("doubtless such things", 65),
                    ),
                    Page(
                        BookLine("have happened often in", 66),
                        BookLine("the busy city that is", 67),
                        BookLine("Varrock. It was expected", 68),
                        BookLine("that Dionysius would", 69),
                        BookLine("become a priest, but", 70),
                        BookLine("he seems to have had", 71),
                        BookLine("other plans..", 72),
                        BookLine("At the age of 15,", 74),
                        BookLine("the age at which children", 75),
                        BookLine("of that era completed", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("their education,", 55),
                        BookLine("Dionysius is reported", 56),
                        BookLine("to have turned down a", 57),
                        BookLine("prestigious invitation", 58),
                        BookLine("to study on the holy", 59),
                        BookLine("island of Entrana.", 60),
                        BookLine("Instead he declared a", 62),
                        BookLine("wish to travel the", 63),
                        BookLine("world before settling", 64),
                    ),
                    Page(
                        BookLine("to a permanent career.", 66),
                        BookLine("The path trodden by", 67),
                        BookLine("the young Dionysius is", 68),
                        BookLine("a winding one. The older", 69),
                        BookLine("dwarves living near", 70),
                        BookLine("Keldagrim remember a", 71),
                        BookLine("youth of that name", 72),
                        BookLine("travelling through their", 73),
                        BookLine("mines at that time.", 74),
                        BookLine("They claim that he gave", 75),
                        BookLine("them great assistance in", 76),
                        BookLine("defending the mines from", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("a rogue tribe of trolls,", 56),
                        BookLine("eventually driving the", 57),
                        BookLine("trolls out of those", 58),
                        BookLine("mines completely.", 59),
                        BookLine("It is believed that", 61),
                        BookLine("the young Dionysius", 62),
                        BookLine("travelled amongst the", 63),
                        BookLine("mountain tribes after", 64),
                        BookLine("he left the dwarves.", 65),
                    ),
                    Page(
                        BookLine("One tribe, now disbanded,", 66),
                        BookLine("used to tell of a strange", 67),
                        BookLine("mystical warrior named", 68),
                        BookLine("'Deesus' - surely a", 69),
                        BookLine("corruption of 'Dionysius'", 70),
                        BookLine("- who helped defend their", 71),
                        BookLine("camp from the monsters", 72),
                        BookLine("that roam the northern", 73),
                        BookLine("lands.", 74),
                        BookLine("'Deesus' was said to be", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("an invincible warrior", 55),
                        BookLine("who wielded a powerful", 56),
                        BookLine("staff.", 57),
                        BookLine("Dionysius subsequently", 59),
                        BookLine("travelled across the", 60),
                        BookLine("seas to the tropical", 61),
                        BookLine("island of Karamja,", 62),
                        BookLine("where some of the oldest", 63),
                        BookLine("natives tell of how he", 64),
                        BookLine("brought with him strange", 65),
                    ),
                    Page(
                        BookLine("herbs that increased", 66),
                        BookLine("their powers in combat.", 67),
                        BookLine("He is also remembered", 69),
                        BookLine("for venturing into the", 70),
                        BookLine("deep caves underneath", 71),
                        BookLine("that island, where very", 72),
                        BookLine("few dare to tread. One", 73),
                        BookLine("Karamjan family claims", 74),
                        BookLine("that a pale-skinned", 75),
                        BookLine("shaman' visited their", 76),
                        BookLine("village a few generations", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("ago and told them to build", 56),
                        BookLine("a hut over the entrance", 57),
                        BookLine("to the caves and demand", 58),
                        BookLine("money from anyone wishing", 59),
                        BookLine("to enter; this 'shaman'", 60),
                        BookLine("may have been Dionysius.", 61),
                        BookLine("Even in the depths of", 63),
                        BookLine("the Kharidian desert,", 64),
                        BookLine("some still remember a", 65),
                    ),
                    Page(
                        BookLine("stranger who lived with", 66),
                        BookLine("the tent-dwelling Bedabin", 67),
                        BookLine("tribe for a while, studying", 68),
                        BookLine("the great mysterious", 69),
                        BookLine("pyramid of the desert.", 70),
                        BookLine("No-one could make him speak", 72),
                        BookLine("of what he discovered about", 73),
                        BookLine("the pyramid or the desert", 74),
                        BookLine("bandits who live near it.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("By now an adult, Dionysius", 55),
                        BookLine("returned to the civilised", 56),
                        BookLine("lands and finally travelled", 57),
                        BookLine("to Entrana. Always a tough", 58),
                        BookLine("youth, he had now learnt", 59),
                        BookLine("great magical powers, and", 60),
                        BookLine("many on Entrana recall that", 61),
                        BookLine("they were in awe of his", 62),
                        BookLine("abilities, venturing", 63),
                        BookLine("fearlessly into the", 64),
                    ),
                    Page(
                        BookLine("cave under that", 66),
                        BookLine("The rest of the pages", 67),
                        BookLine("seem to be missing.", 68),
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
        on(Items.HERMANS_BOOK_7951, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
