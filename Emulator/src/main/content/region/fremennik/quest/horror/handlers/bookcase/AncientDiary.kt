package content.region.fremennik.quest.horror.handlers.bookcase

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class AncientDiary : InteractionListener {
    /*
    * Ancient diary located at Lighthouse.
    * Authentic state.
    */
    companion object {
        private val TITLE = "Diary"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("I do not know why it is", 55),
                        BookLine("me that has been chosen", 56),
                        BookLine("by them... but I will not", 57),
                        BookLine("fail them!", 58),
                        BookLine("Soft whispers, beckoning", 60),
                        BookLine("me at night... I know", 61),
                        BookLine("what you are!", 62),
                    ),
                    Page(
                        BookLine("I saw one today.", 68),
                        BookLine("Magnificence. Is this", 71),
                        BookLine("then, what we all aspire to", 72),
                        BookLine("someday become? Are", 73),
                        BookLine("these beings even natural,", 74),
                        BookLine("or are they beyond what", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("we think of as nature?", 56),
                        BookLine("The beautiful chittering", 58),
                        BookLine("Dagannoth! I know thee!", 60),
                        BookLine("I call thee! For I too am", 61),
                        BookLine("of the ocean, I too know", 62),
                        BookLine("this land as home!", 63),
                    ),
                    Page(
                        BookLine("Home? Perhaps. Where", 68),
                        BookLine("did you come from?", 69),
                        BookLine("Where do you plan to", 70),
                        BookLine("go?", 71),
                        BookLine("You cannot leave. You", 73),
                        BookLine("MUST not leave. I will", 74),
                        BookLine("ensure it.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("I find myself thinking", 56),
                        BookLine("often of the time when", 57),
                        BookLine("we can all be together as", 58),
                        BookLine("one in the deep sea.", 59),
                        BookLine("Problems arose. Can you", 61),
                        BookLine("not see what I want?", 62),
                        BookLine("Can you not see how we", 63),
                        BookLine("should proceed?", 64),
                    ),
                    Page(
                        BookLine("I have no solution.", 70),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("I sleep during the days", 55),
                        BookLine("now, my nights are only", 56),
                        BookLine("for you and your kind.", 57),
                        BookLine("I am thinking of a door.", 59),
                        BookLine("We cannot let everyone", 62),
                        BookLine("know of my plans!", 63),
                    ),
                    Page(
                        BookLine("I was buying food in the", 66),
                        BookLine("nearby town today, the", 67),
                        BookLine("idiot barbarians look at", 68),
                        BookLine("me as though I have lost", 69),
                        BookLine("my wits.", 70),
                        BookLine("Ha! Like they know what", 75),
                        BookLine("wits even are!", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Do they even", 56),
                        BookLine("understand? Of course", 57),
                        BookLine("not they are just", 58),
                        BookLine("barbarians! Soon.", 59),
                        BookLine("Things fall into place so", 64),
                        BookLine("quickly...", 65),
                    ),
                    Page(
                        BookLine("A door. Yes. Definitely a", 66),
                        BookLine("door. But how to keep", 67),
                        BookLine("my secrets secure?", 68),
                        BookLine("Disturbing message from", 70),
                        BookLine("the council today, they", 71),
                        BookLine("feel that I am not", 72),
                        BookLine("fulfilling my contract to", 73),
                        BookLine("keep this lighthouse", 74),
                        BookLine("operative correctly.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Do they think I care?", 58),
                        BookLine("Do I even care", 60),
                        BookLine("anymore?", 61),
                        BookLine("Things proceed apace.", 63),
                        BookLine("I will begin work on the", 64),
                    ),
                    Page(
                        BookLine("door as soon as I can.", 55),
                        BookLine("What can they do to me?", 59),
                        BookLine("Your songs of the night", 62),
                        BookLine("call to me, always.", 63),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Always the chittering.", 68),
                        BookLine("Did I name you,", 70),
                        BookLine("Dagannoth? Or did you", 71),
                        BookLine("tell me your name in my", 72),
                        BookLine("dreams of you?", 73),
                        BookLine("If I named you, did I also", 74),
                        BookLine("create you just by", 75),
                        BookLine("naming you? Am I then", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("also somehow a part of", 56),
                        BookLine("you, or are you a part of", 57),
                        BookLine("me?", 58),
                        BookLine("Things seem so strange", 60),
                        BookLine("nowadays...", 61),
                        BookLine("The door is the key. But", 63),
                        BookLine("what should then be the", 64),
                        BookLine("key to the door?", 65),
                    ),
                    Page(
                        BookLine("I do not think I am well", 66),
                        BookLine("anymore, it is good to", 67),
                        BookLine("have company.", 68),
                        BookLine("Stupid council! Always", 70),
                        BookLine("interfering! You do not", 71),
                        BookLine("know the dagannoth, you", 72),
                        BookLine("have no sway here!", 73),
                        BookLine("I am the sea.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("I see that I am.", 55),
                        BookLine("The door. Dagannoth.", 57),
                        BookLine("Will I wake one day and", 58),
                        BookLine("find that my life so far", 59),
                        BookLine("has been naught but a", 60),
                        BookLine("dream, and that under", 61),
                        BookLine("my skin, under my", 62),
                        BookLine("bones, I too am", 63),
                        BookLine("dagannoth?", 64),
                    ),
                    Page(
                        BookLine("Such dreams I have in", 66),
                        BookLine("the days while I sleep.", 67),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The chittering is now", 55),
                        BookLine("always ringing in my", 56),
                        BookLine("ears... what are you", 57),
                        BookLine("saying to me??", 58),
                        BookLine("The key IS the door!", 60),
                        BookLine("The red of flame, the", 61),
                        BookLine("blue of water. The white", 62),
                        BookLine("of air and the brown of", 63),
                        BookLine("earth.", 64),
                    ),
                    Page(
                        BookLine("Is this all? Can this be?", 68),
                        BookLine("No, dagannoth, it isn't.", 72),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The colours are of deep", 55),
                        BookLine("significance, deep seas,", 56),
                        BookLine("deep thoughts, thoughts of", 57),
                        BookLine("creation, am I a creation,", 58),
                        BookLine("dagannoth? Are you?", 59),
                        BookLine("Then who by?", 60),
                    ),
                    Page(
                        BookLine("And what for?", 69),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("The door is the key. The", 55),
                        BookLine("key to the door is key.", 56),
                        BookLine("Red and yellow and blue", 57),
                        BookLine("and brown and white and", 58),
                        BookLine("green, put them together", 59),
                        BookLine("and what have you got?", 60),
                        BookLine("The runes. Fire, air,", 61),
                        BookLine("water, earth. The other", 62),
                        BookLine("keys. Weapons.", 63),
                        BookLine("I take two other parts of", 65),
                    ),
                    Page(
                        BookLine("the mixture; the sword of", 66),
                        BookLine("the warrior and the", 67),
                        BookLine("arrow of the huntsman.", 68),
                        BookLine("Can this then keep you", 69),
                        BookLine("bound to this place,", 70),
                        BookLine("dagannoth?", 71),
                        BookLine("We shall see.", 73),
                        BookLine("We shall see the sea.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Days are a stream of", 55),
                        BookLine("people, buying, selling can", 56),
                        BookLine("you not leave me be?", 57),
                        BookLine("I think I understand.", 59),
                        BookLine("I come to thee tonight,", 61),
                        BookLine("dagannoth. I know not", 62),
                        BookLine("how this ends, but will", 63),
                        BookLine("now found out. Farewell", 64),
                        BookLine("land, the sea is now my", 65),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("home, my place, my life.", 55),
                    ),
                    Page(
                        BookLine("I have not much time left", 66),
                        BookLine("in this place.", 67),
                        BookLine("Ireturn to you in the sea", 69),
                        BookLine("at night.", 70),
                        BookLine("Dagannoth. Me. Can you", 73),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("become me?", 56),
                        BookLine("Or...", 57),
                        BookLine("Can I somehow... Become", 59),
                        BookLine("you?", 60),
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
        player.packetDispatch.sendString(
            "",
            BookInterface.FANCY_BOOK_3_49,
            BookInterface.FANCY_BOOK_3_49_LINE_IDS[1],
        )
        player.packetDispatch.sendString(
            "",
            BookInterface.FANCY_BOOK_3_49,
            BookInterface.FANCY_BOOK_3_49_LINE_IDS[2],
        )
        return true
    }

    override fun defineListeners() {
        on(Items.DIARY_3846, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
