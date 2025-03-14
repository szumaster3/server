package content.global.handlers.item.book.tome

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class CrumblingTome : InteractionListener {
    /*
     * The crumbling tome, also known as Legend of the Brothers,
     * is found on a desk in a building just east of the entrance
     * to the Barrows.
     */

    companion object {
        private const val TITLE = "Legend of the Brothers"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("During the great god wars", 55),
                        BookLine("there were six brothers", 56),
                        BookLine("who fought side by side", 57),
                        BookLine("against the forces of", 58),
                        BookLine("evil. Their fervor in", 59),
                        BookLine("battle was awed and feared", 60),
                        BookLine("by all who saw them. Nothing", 61),
                        BookLine("is known of their origins,", 62),
                        BookLine("only that they were first", 63),
                        BookLine("seen when the legions", 64),
                        BookLine("of evil were pressing", 65),
                    ),
                    Page(
                        BookLine("their hardest into civilised", 66),
                        BookLine("lands. It was the eldest", 67),
                        BookLine("who struck the first blow", 68),
                        BookLine("in that battle, hurling", 69),
                        BookLine("powerful battle magic", 70),
                        BookLine("into the enemies' ranks,", 71),
                        BookLine("opening a hole for four", 72),
                        BookLine("of the other brothers", 73),
                        BookLine("to charge through and", 74),
                        BookLine("into the heart of their", 75),
                        BookLine("forces. Little was seen", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("or known of the sixth", 55),
                        BookLine("brother at the time, only", 56),
                        BookLine("that when one of the other", 57),
                        BookLine("brothers looked in danger", 58),
                        BookLine("his foe would be felled", 59),
                        BookLine("by a shower of crossbow", 60),
                        BookLine("bolts. It wasn't until", 61),
                        BookLine("the dust had settled that", 62),
                        BookLine("those left could look", 63),
                        BookLine("on their saviours, the", 64),
                        BookLine("six brothers who stood", 65),
                    ),
                    Page(
                        BookLine("proud with the bodies", 66),
                        BookLine("of the fallen at their", 67),
                        BookLine("feet. For years these", 68),
                        BookLine("brothers fought hard against", 69),
                        BookLine("the enemy, throwing themselves", 70),
                        BookLine("into battle with little", 71),
                        BookLine("regard for their own safety", 72),
                        BookLine("and relying only on each", 73),
                        BookLine("other for aid. Stories", 74),
                        BookLine("were told around camp", 75),
                        BookLine("fires by soldiers of the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("feats they'd witnessed,", 55),
                        BookLine("they became legend long", 56),
                        BookLine("before the end. Some", 57),
                        BookLine("say he'd always been there,", 58),
                        BookLine("but no-one really knows", 59),
                        BookLine("for sure. The first real", 60),
                        BookLine("tale was after an incursion", 61),
                        BookLine("deep into the dark lands,", 62),
                        BookLine("where once again the brothers", 63),
                        BookLine("proved their worth. It", 64),
                        BookLine("was after the battle that", 65),
                    ),
                    Page(
                        BookLine("a bent and robed figure", 66),
                        BookLine("was seen amongst the bodies", 67),
                        BookLine("of the dead. Most took", 68),
                        BookLine("him for a scavenger, but", 69),
                        BookLine("he moved towards the camp", 70),
                        BookLine("fire of the brothers with", 71),
                        BookLine("such directness that some", 72),
                        BookLine("at first thought him another", 73),
                        BookLine("companion. The brothers", 74),
                        BookLine("however reacted as with", 75),
                        BookLine("suspicion at his approach,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("drawing their weapons", 55),
                        BookLine("and rising from their", 56),
                        BookLine("places. The stranger merely", 57),
                        BookLine("stood before them, as", 58),
                        BookLine("though sizing them up", 59),
                        BookLine("before turning and walking", 60),
                        BookLine("back into the mists. Some", 61),
                        BookLine("say that it's the first", 62),
                        BookLine("time they'd ever seen", 63),
                        BookLine("the brothers unsure. As", 64),
                        BookLine("the campaign moved deeper", 65),
                    ),
                    Page(
                        BookLine("into the dark lands the", 66),
                        BookLine("stranger appeared with", 67),
                        BookLine("greater frequency, often", 68),
                        BookLine("appearing on a rise near", 69),
                        BookLine("a battle and watching", 70),
                        BookLine("over it. The most disturbing", 71),
                        BookLine("thing was how his presence", 72),
                        BookLine("started to affect the", 73),
                        BookLine("brothers, they became", 74),
                        BookLine("distracted and on occasion", 75),
                        BookLine("even received injuries", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("that required attention. It", 55),
                        BookLine("was deep in the heart", 56),
                        BookLine("of the enemies' territory,", 57),
                        BookLine("in the failing light of", 58),
                        BookLine("dusk, that the enemy struck", 59),
                        BookLine("back. Great monstrosities", 60),
                        BookLine("swept through the camps", 61),
                        BookLine("tearing at all in their", 62),
                        BookLine("path, soldiers scattered", 63),
                        BookLine("like chaff in the wind", 64),
                        BookLine("before the onslaught.", 65),
                    ),
                    Page(
                        BookLine("The brothers, valiant", 66),
                        BookLine("as ever, stood back to", 67),
                        BookLine("back before the enemy", 68),
                        BookLine("and fought with all their", 69),
                        BookLine("might. As dawn broke the", 70),
                        BookLine("enemy fled the battle,", 71),
                        BookLine("moving faster than any", 72),
                        BookLine("man could give chase.", 73),
                        BookLine("It was as the soldiers", 74),
                        BookLine("went about rebuilding", 75),
                        BookLine("the camp that the brothers", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("collapsed at their camp", 55),
                        BookLine("fire, they lay as tough", 56),
                        BookLine("yet breathing muttering", 57),
                        BookLine("in strange tongues. Their", 58),
                        BookLine("injuries were found swollen", 59),
                        BookLine("and infected, none of", 60),
                        BookLine("the physicians knew what", 61),
                        BookLine("to do, it was like no", 62),
                        BookLine("infection they had ever", 63),
                        BookLine("seen before. all through", 64),
                        BookLine("the day the brothers were", 65),
                    ),
                    Page(
                        BookLine("tended and cared for while", 66),
                        BookLine("the soldiers waited nervously", 67),
                        BookLine("for their heroes to rise", 68),
                        BookLine("once again. It was then,", 69),
                        BookLine("just as the sun's last", 70),
                        BookLine("rays vanished behind the", 71),
                        BookLine("horizon, that all hope", 72),
                        BookLine("was lost. As if on cue", 73),
                        BookLine("all the six brothers sighed", 74),
                        BookLine("one last breath and died.", 75),
                        BookLine("It was only then that", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("the stranger once again,", 55),
                        BookLine("standing on a rise just", 56),
                        BookLine("outside the camp. That", 57),
                        BookLine("night the camp was surrounded", 58),
                        BookLine("by the great beasts of", 59),
                        BookLine("the enemy once again,", 60),
                        BookLine("but they didn't attack,", 61),
                        BookLine("they merely baited the", 62),
                        BookLine("soldiers to come out and", 63),
                        BookLine("meet them. Once again", 64),
                        BookLine("at the break of dawn the", 65),
                    ),
                    Page(
                        BookLine("enemy fled without a trace,", 66),
                        BookLine("leaving the soldiers tired", 67),
                        BookLine("from a restless night", 68),
                        BookLine("to worry and fear. That", 69),
                        BookLine("day the commanders decided", 70),
                        BookLine("to turn back, they could", 71),
                        BookLine("advance no further without", 72),
                        BookLine("the brothers' aid. As", 73),
                        BookLine("was the custom at the", 74),
                        BookLine("time, out of honour and", 75),
                        BookLine("respect for the mighty", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("fallen, they constructed", 55),
                        BookLine("six great barrows for", 56),
                        BookLine("the brothers. A crypt", 57),
                        BookLine("for each brother covered", 58),
                        BookLine("in a small hill of earth", 59),
                        BookLine("that would hold their", 60),
                        BookLine("remains undisturbed by", 61),
                        BookLine("predators. The work took", 62),
                        BookLine("many days, for each night", 63),
                        BookLine("the enemy returned and", 64),
                        BookLine("the soldiers had to flee", 65),
                    ),
                    Page(
                        BookLine("back inside their camp", 66),
                        BookLine("to wait away the night", 67),
                        BookLine("to the sounds of howling", 68),
                        BookLine("and scratching at the", 69),
                        BookLine("fences. But each day they", 70),
                        BookLine("resumed their work, their", 71),
                        BookLine("sense of duty to the brothers", 72),
                        BookLine("surmounting their fear", 73),
                        BookLine("of the dark forces in", 74),
                        BookLine("the land. On the last", 75),
                        BookLine("day the assembled troops", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("buried the brothers inside", 55),
                        BookLine("their stone tombs, then", 56),
                        BookLine("with great reverence,", 57),
                        BookLine("but little ceremony, they", 58),
                        BookLine("covered the tombs with", 59),
                        BookLine("earth. As the army broke", 60),
                        BookLine("camp and turned to march", 61),
                        BookLine("back to their lands the", 62),
                        BookLine("stranger was seen once", 63),
                        BookLine("again, standing atop the", 64),
                        BookLine("central barrow... standing", 65),
                    ),
                    Page(
                        BookLine("tall against the failing", 66),
                        BookLine("light with his arms outstretched", 67),
                        BookLine("to the sky. Not many", 68),
                        BookLine("of the original army made", 69),
                        BookLine("it back to these lands,", 70),
                        BookLine("many more had perished", 71),
                        BookLine("on the journey home, but", 72),
                        BookLine("tales were told of how", 73),
                        BookLine("each night an unnatural", 74),
                        BookLine("glow could be seen on", 75),
                        BookLine("the horizon where they", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("had buried the brothers.", 55),
                        BookLine("Even today there are stories", 56),
                        BookLine("told about those mounds", 57),
                        BookLine("of earth, and of the sights", 58),
                        BookLine("and sounds that have been", 59),
                        BookLine("witnessed nearby.", 60),
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
        on(Items.CRUMBLING_TOME_4707, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
