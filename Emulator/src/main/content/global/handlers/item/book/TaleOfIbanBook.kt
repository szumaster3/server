package content.global.handlers.item.book

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class TaleOfIbanBook : InteractionListener {
    /*
     * Obtainable during the Underground pass quest.
     */

    companion object {
        private const val TITLE = "The Tale of Iban"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Introduction Gather round,", 55),
                        BookLine("all ye followers of the", 56),
                        BookLine("dark arts. Read carefully", 57),
                        BookLine("the words that I hearby", 58),
                        BookLine("inscribe, as I detail the", 59),
                        BookLine("heady brew that is responsible", 60),
                        BookLine("for my greatest creation", 61),
                        BookLine("in all my time on this", 62),
                        BookLine("world. I am Kardia, the", 63),
                        BookLine("most wretched witch in", 64),
                        BookLine("the land; scorned by beauty,", 65),
                    ),
                    Page(
                        BookLine("the world and its inhabitants,", 66),
                        BookLine("see what I created: The", 67),
                        BookLine("most fearsome and powerful", 68),
                        BookLine("force of darkness the like", 69),
                        BookLine("of which has never before", 70),
                        BookLine("been seen in this world,", 71),
                        BookLine("in human form... Iban", 72),
                        BookLine("was a Black Knight who", 73),
                        BookLine("had learned to fight under", 74),
                        BookLine("the great Daquarius, Lord", 75),
                        BookLine("of the Black Knights. Together,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("they had taken on the might", 55),
                        BookLine("of the White and the blood", 56),
                        BookLine("of a hundred soldiers had", 57),
                        BookLine("been wiped from the sword", 58),
                        BookLine("of Iban. Iban was not", 59),
                        BookLine("quite so different from", 60),
                        BookLine("those who tasted his blade:", 61),
                        BookLine("noble and educated, with", 62),
                        BookLine("a taste for the finer things", 63),
                        BookLine("available in life. But", 64),
                        BookLine("there was something that", 65),
                    ),
                    Page(
                        BookLine("made him different: Ambition.", 66),
                        BookLine("This hunger for more went", 67),
                        BookLine("far past the realm of mere", 68),
                        BookLine("mortals, into the shadowy", 69),
                        BookLine("places of darkness and", 70),
                        BookLine("evil. Iban's ambition", 71),
                        BookLine("was almost godlike in its", 72),
                        BookLine("insatiability, but therein", 73),
                        BookLine("lay the essence of his", 74),
                        BookLine("darkness: at its most base", 75),
                        BookLine("Iban's fundamental desire", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("was to control the hearts", 55),
                        BookLine("and minds of his fellow", 56),
                        BookLine("men, to take them beyond", 57),
                        BookLine("the pale of mere allegiance,", 58),
                        BookLine("and corrupt them into a", 59),
                        BookLine("force for evil. A whole", 60),
                        BookLine("legion of these Soulless", 61),
                        BookLine("beings, their minds demented", 62),
                        BookLine("from the sheer power of", 63),
                        BookLine("darkness that channelled", 64),
                        BookLine("through them... Zombies,", 65),
                    ),
                    Page(
                        BookLine("void of emotions, without", 66),
                        BookLine("feelings or cares, Servants", 67),
                        BookLine("to their wicked master", 68),
                        BookLine("even unto death... But", 69),
                        BookLine("dreams were all they ever", 70),
                        BookLine("were. As a mere mortal,", 71),
                        BookLine("heroic though he was, this", 72),
                        BookLine("ambition Iban was unable", 73),
                        BookLine("to achieve. Meeting his", 74),
                        BookLine("demise in the White Knights'", 75),
                        BookLine("now famous Dawn Ascent,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Iban died with the bitter", 55),
                        BookLine("taste of failure in his", 56),
                        BookLine("mouth. Little did he know", 57),
                        BookLine("that death was only just", 58),
                        BookLine("the beginning... The Vow I", 59),
                        BookLine("knew of Iban's life, though", 60),
                        BookLine("of course I had not met", 61),
                        BookLine("him. Using the power of", 62),
                        BookLine("my dark practices, I vowed", 63),
                        BookLine("to resurrect this greatest", 64),
                        BookLine("of warriors. I would raise", 65),
                    ),
                    Page(
                        BookLine("him again to fulfill the", 66),
                        BookLine("promise of his human life.", 67),
                        BookLine("To be a master... ...of", 68),
                        BookLine("the undead. The Elements:", 69),
                        BookLine("Flesh Taking a small doll", 70),
                        BookLine("with the likeness of Iban", 71),
                        BookLine("I smeared my effigy with", 72),
                        BookLine("the four elements that", 73),
                        BookLine("together bring existence", 74),
                        BookLine("into being. Essence of", 75),
                        BookLine("his darkness. Upon his", 76),
                        BookLine("death, I claimed Iban's", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("body from the battlefield", 56),
                        BookLine("where he lay. I had him", 57),
                        BookLine("buried in a great tomb", 58),
                        BookLine("at the bottom of the caverns.", 59),
                        BookLine("Before I did though, I", 60),
                        BookLine("took a piece of Iban's", 61),
                        BookLine("cold flesh. Clasping some", 62),
                        BookLine("in my hand, I smeared it", 63),
                        BookLine("over the doll, and chanted", 64),
                        BookLine("his name with mighty incantation.", 65),
                    ),
                    Page(
                        BookLine("The Elements: Blood I also", 66),
                        BookLine("needed blood, the giver", 67),
                        BookLine("of life force. By now Iban's", 68),
                        BookLine("body was but a hardened", 69),
                        BookLine("vessel, the blood drained", 70),
                        BookLine("empty. However, on the", 71),
                        BookLine("lowest level of these caverns", 72),
                        BookLine("there lives a giant spider", 73),
                        BookLine("known as Kalrag. The hunger", 74),
                        BookLine("of Kalrag is great, and", 75),
                        BookLine("it is known to have fed", 76),
                        BookLine("on the warm blood of many", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("a human. I claimed some", 56),
                        BookLine("of Kalrag's blood and wiped", 57),
                        BookLine("it onto the effigy of Iban", 58),
                        BookLine("that I had fashioned. The", 59),
                        BookLine("blood of Kalrag is now", 60),
                        BookLine("the blood of Iban. The", 61),
                        BookLine("Elements: Shadow Then came", 62),
                        BookLine("the hard part, recreating", 63),
                        BookLine("the parts of a man that", 64),
                        BookLine("cannot be seen or touched:", 65),
                    ),
                    Page(
                        BookLine("Those intangible things", 66),
                        BookLine("the essence of life itself.", 67),
                        BookLine("On the platforms west of", 68),
                        BookLine("my home, under terrible", 69),
                        BookLine("strain, I performed the", 70),
                        BookLine("ancient ritual of Incantia.", 71),
                        BookLine("It is an undertaking so", 72),
                        BookLine("dark, and so powerful,", 73),
                        BookLine("that the life was nearly", 74),
                        BookLine("stolen from my body. When", 75),
                        BookLine("I recovered, I saw three", 76),
                        BookLine("Demons summonned, standing", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("in a triangle, their energy", 56),
                        BookLine("focused on the doll of", 57),
                        BookLine("Iban. These Demons were", 58),
                        BookLine("the keepers of Iban's shadow,", 59),
                        BookLine("forever bound to him... The", 60),
                        BookLine("Elements: Conscience Finally,", 61),
                        BookLine("I had to make that most", 62),
                        BookLine("unique thing, the one element", 63),
                        BookLine("that separates man from", 64),
                        BookLine("all other beasts - his", 65),
                    ),
                    Page(
                        BookLine("Conscience. A Zombie has", 66),
                        BookLine("no mind: a creature borne", 67),
                        BookLine("of bloodlust, destruction.", 68),
                        BookLine("But for all Iban's life", 69),
                        BookLine("he chose to take the path", 70),
                        BookLine("of darkness, the road to", 71),
                        BookLine("evil. Driven by this unholy", 72),
                        BookLine("ambition, his potential", 73),
                        BookLine("grew and now I could harness", 74),
                        BookLine("the residue of his existence,", 75),
                        BookLine("that remained trapped in", 76),
                        BookLine("the dark places, to the", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("fullest. Locked inside", 56),
                        BookLine("an old wooden cage sat", 57),
                        BookLine("a beautiful white dove.", 58),
                        BookLine("A symbol of peace, freedom", 59),
                        BookLine("and hope, but also oblivious", 60),
                        BookLine("to the darkness of the", 61),
                        BookLine("world, like a newborn child.", 62),
                        BookLine("Taking the dove with me,", 63),
                        BookLine("I cradled the thing in", 64),
                        BookLine("my arms, stroking its soft", 65),
                    ),
                    Page(
                        BookLine("downy feathers. I looked", 66),
                        BookLine("into the eyes of the bird,", 67),
                        BookLine("and gently placing a kiss", 68),
                        BookLine("upon its fragile head,", 69),
                        BookLine("I then strangled the bird,", 70),
                        BookLine("taking its life between", 71),
                        BookLine("my callous fingers. Truly", 72),
                        BookLine("this bird would be the", 73),
                        BookLine("conscience of Iban: innocence", 74),
                        BookLine("corrupted by evil... Taking", 75),
                        BookLine("crushed bones from the", 76),
                        BookLine("dove's body, I cast my", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("mind's eye onto the body", 56),
                        BookLine("of Iban. One of the servants", 57),
                        BookLine("took the remains of the", 58),
                        BookLine("dove. I let them keep it,", 59),
                        BookLine("for I had no use of it.", 60),
                        BookLine("However, I locked them", 61),
                        BookLine("in a cage on the northern", 62),
                        BookLine("platforms so that they", 63),
                        BookLine("may think on their sins...", 64),
                        BookLine("The Resurrection My ritual", 65),
                    ),
                    Page(
                        BookLine("was complete, soon he would", 66),
                        BookLine("come again renewed with", 67),
                        BookLine("life. I, Kardia, had done", 68),
                        BookLine("the unimagineable: Iban", 69),
                        BookLine("was resurrected, the most", 70),
                        BookLine("powerful evil to take human", 71),
                        BookLine("form. I alone knew that", 72),
                        BookLine("the same process that I", 73),
                        BookLine("had used to resurrect the", 74),
                        BookLine("soul of Iban could be used", 75),
                        BookLine("to destroy that very same", 76),
                        BookLine("evil. But now I was tired,", 55),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("as I closed my eyes, I", 55),
                        BookLine("was contented by the thought", 56),
                        BookLine("of the evil to be unleashed...", 57),
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
        on(Items.HISTORY_OF_IBAN_1494, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
