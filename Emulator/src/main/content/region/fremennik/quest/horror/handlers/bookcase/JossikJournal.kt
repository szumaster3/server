package content.region.fremennik.quest.horror.handlers.bookcase

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class JossikJournal : InteractionListener {
    /*
     * Jossik diary located at Lighthouse.
     * Authentic state.
     */
    companion object {
        private val TITLE = "Diary"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Bennath the 3rd", 55),
                        BookLine("The mystery of what has", 56),
                        BookLine("happened to my uncle", 57),
                        BookLine("has still not been", 58),
                        BookLine("revealed,", 59),
                        BookLine("but I must dwell on", 60),
                        BookLine("it no longer. It is", 61),
                        BookLine("slightly unfair that", 62),
                        BookLine("due to his absence the", 63),
                        BookLine("council have forced", 64),
                        BookLine("me to take up his job", 65),
                    ),
                    Page(
                        BookLine("here at the lighthouse,", 66),
                        BookLine("but what can I say to", 67),
                        BookLine("argue against them? They", 68),
                        BookLine("have allowed me to make", 69),
                        BookLine("a small income by selling", 70),
                        BookLine("items without the usual", 71),
                        BookLine("sales tax, but who is", 72),
                        BookLine("ever going to come all", 73),
                        BookLine("the way out to this", 74),
                        BookLine("forsaken spot? Worse,", 75),
                        BookLine("I fear for my life,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("being between two barbarian", 55),
                        BookLine("camps!I have not had", 56),
                        BookLine("any particular problems", 57),
                        BookLine("with the locals so far", 58),
                        BookLine("but who knows what can", 59),
                        BookLine("set off a barbarian???", 60),
                        BookLine("Bennath the 15th", 61),
                        BookLine("", 62),
                        BookLine("My fears seem", 63),
                        BookLine("to have been unfounded,", 64),
                        BookLine("the local barbarians,", 65),
                    ),
                    Page(
                        BookLine("who I have learnt prefer", 66),
                        BookLine("the term 'Fremennik',", 67),
                        BookLine("seem to be decent and", 68),
                        BookLine("hard working people", 69),
                        BookLine("after all. They certainly", 70),
                        BookLine("do not live up to the", 71),
                        BookLine("terrible things that", 72),
                        BookLine("I have heard about them", 73),
                        BookLine("before now! I still", 74),
                        BookLine("do not have any clues", 75),
                        BookLine("as to what has happened", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to my uncle... Some", 55),
                        BookLine("of the local folks have", 56),
                        BookLine("tried to suggest that", 57),
                        BookLine("he was eaten by a sea", 58),
                        BookLine("monster that they claim", 59),
                        BookLine("lives near here, but", 60),
                        BookLine("I hold no weight to", 61),
                        BookLine("their foolish superstitions!", 62),
                        BookLine("He was always slightly", 63),
                        BookLine("eccentric, and I fear", 64),
                        BookLine("that he was simply overcome", 65),
                    ),
                    Page(
                        BookLine("by the terrible loneliness", 66),
                        BookLine("from living out in this", 67),
                        BookLine("desolate lighthouse,", 68),
                        BookLine("and one night simply", 69),
                        BookLine("gave himself up to the", 70),
                        BookLine("sea...", 71),
                        BookLine("", 72),
                        BookLine("Bennath the 32nd", 73),
                        BookLine("It has now been", 74),
                        BookLine("almost a month since", 75),
                        BookLine("first I was sent to", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("this forsaken and desolate", 55),
                        BookLine("spot. I still have", 56),
                        BookLine("no clue what happened", 57),
                        BookLine("to my uncle, and frankly,", 58),
                        BookLine("I have almost stopped", 59),
                        BookLine("really caring. All", 60),
                        BookLine("I can think of is somehow", 61),
                        BookLine("getting away from this", 62),
                        BookLine("terrible place... it", 63),
                        BookLine("has been over two weeks", 64),
                        BookLine("now since I last saw", 65),
                    ),
                    Page(
                        BookLine("another human face...", 66),
                        BookLine("I do not know how much", 67),
                        BookLine("longer I can stand the", 68),
                        BookLine("loneliness! I do not", 69),
                        BookLine("know if it is my mind", 70),
                        BookLine("playing tricks on me,", 71),
                        BookLine("but recently I have", 72),
                        BookLine("begun to hear something", 73),
                        BookLine("whispered on the wind...", 74),
                        BookLine("it seems to call my", 75),
                        BookLine("name as I fall asleep", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("at night. There have", 55),
                        BookLine("also been many strange", 56),
                        BookLine("noises from below the", 57),
                        BookLine("lighthouse, and from", 58),
                        BookLine("behind the strange metal", 59),
                        BookLine("doorway that my uncle", 60),
                        BookLine("had installed in the", 61),
                        BookLine("basement. Perhaps this", 62),
                        BookLine("is how my uncle felt", 63),
                        BookLine("before he... No, I still", 64),
                        BookLine("do not know what has", 65),
                    ),
                    Page(
                        BookLine("happened to him. I am", 66),
                        BookLine("filled with resolve", 67),
                        BookLine("to discover what exactly", 68),
                        BookLine("did happen - if nothing", 69),
                        BookLine("else, it will keep my", 70),
                        BookLine("mind from the voices", 71),
                        BookLine("that call to me nightly.", 72),
                        BookLine("", 73),
                        BookLine("Raktuber 13th", 74),
                        BookLine("It has been some", 75),
                        BookLine("time last I filled out", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("this journal, for my", 55),
                        BookLine("spirits have been lifted", 56),
                        BookLine("greatly in recent times,", 57),
                        BookLine("and it is all down to", 58),
                        BookLine("a single person ", 59),
                        BookLine("I encountered", 60),
                        BookLine("while working here! Her", 61),
                        BookLine("name is Larrissa and", 62),
                        BookLine("she is a local Fremennik", 63),
                        BookLine("girl, who comes here", 64),
                        BookLine("to buy goods not for", 65),
                    ),
                    Page(
                        BookLine("sale in her home town", 66),
                        BookLine("of Rellekka sometimes. To", 67),
                        BookLine("be honest, I would have", 68),
                        BookLine("tried to strike up a", 69),
                        BookLine("friendship with anyone", 70),
                        BookLine("who came by, for it", 71),
                        BookLine("is a terribly lonely", 72),
                        BookLine("job working at this", 73),
                        BookLine("light- house, but even", 74),
                        BookLine("if I worked in Varrock", 75),
                        BookLine("I could not have chosen", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("a more pleasant companion!", 55),
                        BookLine("We really struck a chord", 56),
                        BookLine("together, and I think", 57),
                        BookLine("we may become more than", 58),
                        BookLine("friends eventually,", 59),
                        BookLine("but I worry what her", 60),
                        BookLine("family will think of", 61),
                        BookLine("me - from what I hear", 62),
                        BookLine("the people of Rellekka", 63),
                        BookLine("do not take kindly to", 64),
                        BookLine("strangers! I have also", 65),
                    ),
                    Page(
                        BookLine("made some progress in", 66),
                        BookLine("my search for the truth", 67),
                        BookLine("of what happened to", 68),
                        BookLine("my uncle Silas all those", 69),
                        BookLine("months ago! I found", 70),
                        BookLine("a secret compartment", 71),
                        BookLine("with a diary hidden", 72),
                        BookLine("within it. I have placed", 73),
                        BookLine("the diary in the bookshelf", 74),
                        BookLine("for later examination.", 75),
                        BookLine("It is definitely his", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("hand writing, and I", 55),
                        BookLine("hope that it may shed", 56),
                        BookLine("some light onto his", 57),
                        BookLine("eventual fate. I cannot", 58),
                        BookLine("help but think that", 59),
                        BookLine("had he never come to", 60),
                        BookLine("this isolated place,", 61),
                        BookLine("that whatever befell", 62),
                        BookLine("him might have never", 63),
                        BookLine("happened...", 64),
                        BookLine("", 65),
                    ),
                    Page(
                        BookLine("Pentember 24th", 66),
                        BookLine("Has it been so long", 67),
                        BookLine("since last I wrote", 68),
                        BookLine("in this thing? These", 69),
                        BookLine("last months have seemed", 70),
                        BookLine("like a glorious dream...", 71),
                        BookLine("Larrissa and I have been", 72),
                        BookLine("slowly falling in love,", 73),
                        BookLine("and the torment that this", 74),
                        BookLine("lighthouse was to me", 75),
                        BookLine("when first I arrived", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("here, is now the place", 55),
                        BookLine("where I have finally", 56),
                        BookLine("found true happiness! All", 57),
                        BookLine("is not as perfect as", 58),
                        BookLine("I would like to believe", 59),
                        BookLine("it to be however. I", 60),
                        BookLine("have still had no luck", 61),
                        BookLine("in understanding what", 62),
                        BookLine("happened to my uncle", 63),
                        BookLine("Silas, and his diary", 64),
                        BookLine("is so bizarre I cannot", 65),
                    ),
                    Page(
                        BookLine("believe that the man", 66),
                        BookLine("who wrote it is the", 67),
                        BookLine("same man who used to", 68),
                        BookLine("take me on fishing trips", 69),
                        BookLine("to Karamja when I was", 70),
                        BookLine("a young lad. The writer", 71),
                        BookLine("seems to have been driven", 72),
                        BookLine("horribly insane by living", 73),
                        BookLine("here, or by some event", 74),
                        BookLine("that he witnessed one", 75),
                        BookLine("day, and his diary is", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("full of strange and", 55),
                        BookLine("cryptic passages... I", 56),
                        BookLine("feel sure that there", 57),
                        BookLine("is some message here,", 58),
                        BookLine("but I just cannot find", 59),
                        BookLine("what it says! My nights", 60),
                        BookLine("alone are getting worse", 61),
                        BookLine("too. During the day", 62),
                        BookLine("when I am with Larrissa,", 63),
                        BookLine("I am always filled with", 64),
                        BookLine("joy, but when she goes", 65),
                    ),
                    Page(
                        BookLine("home to her family at", 66),
                        BookLine("night, and I am left", 67),
                        BookLine("here alone my mind begins", 68),
                        BookLine("to play tricks upon", 69),
                        BookLine("me. Often I think I", 70),
                        BookLine("can hear ...things...", 71),
                        BookLine("moving in the crawlspaces", 72),
                        BookLine("of the lighthouse. Whenever", 73),
                        BookLine("the wind is to the North", 74),
                        BookLine("I can also sometimes", 75),
                        BookLine("hear a dread - ful ", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("chittering noise.", 55),
                        BookLine("Is it voices?", 56),
                        BookLine("Or some creature of", 57),
                        BookLine("the night? I know not,", 58),
                        BookLine("but the sound disturbs", 59),
                        BookLine("me... One night I even", 60),
                        BookLine("thought I heard my uncle's", 61),
                        BookLine("voice calling to me", 62),
                        BookLine("to join him, but the", 63),
                        BookLine("voice was distorted,", 64),
                        BookLine("and gurgled as though", 65),
                    ),
                    Page(
                        BookLine("he were trying to talk", 66),
                        BookLine("through water. I dismissed", 67),
                        BookLine("this as a bad dream,", 68),
                        BookLine("yet it did disturb me", 69),
                        BookLine("deeply. I haven't told", 70),
                        BookLine("Larrissa of my concerns,", 71),
                        BookLine("for I would not wish", 72),
                        BookLine("her to worry about me,", 73),
                        BookLine("but I have decided to", 74),
                        BookLine("give her a spare key", 75),
                        BookLine("to this lighthouse,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("for I cannot shake this", 55),
                        BookLine("feeling that should", 56),
                        BookLine("I remain here much longer", 57),
                        BookLine("something terrible is", 58),
                        BookLine("going to happen to me...", 59),
                        BookLine("Strange!", 60),
                        BookLine("I thought I heard a", 61),
                        BookLine("noise from downstairs", 62),
                        BookLine("just now! But with the", 63),
                        BookLine("front door locked, there", 64),
                        BookLine("is no way for anyone", 65),
                    ),
                    Page(
                        BookLine("to enter the lighthouse!", 66),
                        BookLine("I must investigate...", 67),
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
        player.packetDispatch.sendString("", BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_LINE_IDS[1])
        player.packetDispatch.sendString("", BookInterface.FANCY_BOOK_3_49, BookInterface.FANCY_BOOK_3_49_LINE_IDS[2])
        return true
    }

    override fun defineListeners() {
        on(Items.JOURNAL_3845, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
