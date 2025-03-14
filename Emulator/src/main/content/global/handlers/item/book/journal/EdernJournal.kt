package content.global.handlers.item.book.journal

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import org.rs.consts.Items

class EdernJournal : InteractionListener {
    /*
     * Edern's journal can be found by searching the
     * dead guard that is located outside the Temple of Light.
     */

    companion object {
        private const val TITLE = "Journal of Nissyen Edern"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("I start this new journal as", 55),
                        BookLine("I start my new life, I", 56),
                        BookLine("leave behind all that I", 57),
                        BookLine("was as I take a stand for", 58),
                        BookLine("the Elven race. It is", 59),
                        BookLine("my hope that we can at", 60),
                        BookLine("last find peace with our", 61),
                        BookLine("misguided brethren and", 62),
                        BookLine("return home. to stay here", 63),
                        BookLine("is to condemn ourselves to", 64),
                        BookLine("an eternity of chaos. To", 65),
                    ),
                    Page(
                        BookLine("this end we must each do", 66),
                        BookLine("all that we can, for the", 67),
                        BookLine("greater good.", 68),
                        BookLine("<col=08088A>Year 0 Day 1", 70),
                        BookLine("The Death Guard is an old", 71),
                        BookLine("and honourable society", 72),
                        BookLine("and so I have enrolled,", 73),
                        BookLine("I have 1 more day in the", 74),
                        BookLine("city before I go for", 75),
                        BookLine("training. I was speaking", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("to my brother Iestin,", 55),
                        BookLine("he was telling me", 56),
                        BookLine("of the research he has", 57),
                        BookLine("been doing on the", 58),
                        BookLine("under-city, he thinks", 59),
                        BookLine("he may have found", 60),
                        BookLine("the location of the old", 61),
                        BookLine("Grand Library. It is a", 62),
                        BookLine("shame his research will", 63),
                        BookLine("never amount to anything,", 64),
                        BookLine("no one will ever enter", 65),
                    ),
                    Page(
                        BookLine("that place - the seals", 66),
                        BookLine("were placed on it over", 67),
                        BookLine("2000 years ago by Seren", 68),
                        BookLine("herself. He asked how my", 69),
                        BookLine("historical study of the", 70),
                        BookLine("Eastern Realms was going.", 71),
                        BookLine("I did not have the heart", 72),
                        BookLine("to tell him, I will wait", 73),
                        BookLine("until tomorrow.", 74),
                        BookLine("<col=08088A>Year 0 Day 2", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("I have put the last of", 55),
                        BookLine("my affairs in order.", 56),
                        BookLine("Iestin begged me to", 57),
                        BookLine("not go,telling me that", 58),
                        BookLine("I should stick with", 59),
                        BookLine("my research. I think", 60),
                        BookLine("my big brother may have", 61),
                        BookLine("had his nose in a book", 62),
                        BookLine("too long, he does not see", 63),
                        BookLine("what is happening to our", 64),
                        BookLine("people, we must regain", 65),
                    ),
                    Page(
                        BookLine("our heritage if we are", 66),
                        BookLine("to survive on this world.", 67),
                        BookLine("Besides, if I am ever to", 68),
                        BookLine("know anything about the", 69),
                        BookLine("Eastern Realm I should", 70),
                        BookLine("go and see the land", 71),
                        BookLine("myself. This may be", 72),
                        BookLine("the last night I spend", 73),
                        BookLine("in the home of my", 74),
                        BookLine("ancestors.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Year 2 Day 2", 55),
                        BookLine("It seems a shame to have", 56),
                        BookLine("lost 2 years from my", 57),
                        BookLine("Journal but I can", 58),
                        BookLine("understand the need for", 59),
                        BookLine("secrecy. I am told", 60),
                        BookLine("that when the subjects", 61),
                        BookLine("covered in that portion", 62),
                        BookLine("of my journal are no longer", 63),
                        BookLine("an issue of security the", 64),
                        BookLine("pages will be returned", 65),
                    ),
                    Page(
                        BookLine("to me. At least I finished", 66),
                        BookLine("training, I'm told that", 67),
                        BookLine("my first assignment will", 68),
                        BookLine("be in a human city.", 69),
                        BookLine("I wonder what they look", 70),
                        BookLine("like. I was able to get", 71),
                        BookLine("an hour free in the", 72),
                        BookLine("city before leaving", 73),
                        BookLine("for my first post.", 74),
                        BookLine("I found Iestin in the same", 75),
                        BookLine("library I had left him", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("in 2 years ago. It was", 55),
                        BookLine("good to see him again after", 56),
                        BookLine("such time, although his", 57),
                        BookLine("research has led him to", 58),
                        BookLine("some absurd conclusions.", 59),
                        BookLine("He thinks that one of the", 60),
                        BookLine("9 seals to the under-city", 61),
                        BookLine("is broke. Though I am sure", 62),
                        BookLine("there are too many", 63),
                        BookLine("safeguards for such", 64),
                        BookLine("to be true. I wish", 65),
                    ),
                    Page(
                        BookLine("I had had more time", 66),
                        BookLine("for him. This evening I", 67),
                        BookLine("camp on the peaks", 68),
                        BookLine("of Arandar, tomorrow", 69),
                        BookLine("I report for my", 70),
                        BookLine("first day in service of", 71),
                        BookLine("the Death Guard.", 72),
                        BookLine("<col=08088A>Year 2 Day 3", 74),
                        BookLine("The induction to my new", 75),
                        BookLine("post was poor at best,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("I was not even assigned", 55),
                        BookLine("a bunk. The rest of the", 56),
                        BookLine("day was spent in a watch", 57),
                        BookLine("tower occasionally getting", 58),
                        BookLine("attacked by pigeons. The", 59),
                        BookLine("humans are not so different", 60),
                        BookLine("as I thought they would", 61),
                        BookLine("be, although they live", 62),
                        BookLine("like animals.", 63),
                        BookLine("<col=08088A>Year 2 Day 6", 65),
                    ),
                    Page(
                        BookLine("I have been kept very", 66),
                        BookLine("busy by guard duty over", 67),
                        BookLine("the last few days and", 68),
                        BookLine("this is the first chance", 69),
                        BookLine("I've had to make an entry", 70),
                        BookLine("in my journal. I have", 71),
                        BookLine("bad feelings about the", 72),
                        BookLine("work I undertake. It seems", 73),
                        BookLine("to me that any task that", 74),
                        BookLine("requires us to treat other", 75),
                        BookLine("beings as poorly as we", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("are treating these humans", 55),
                        BookLine("can only come to ill,", 56),
                        BookLine("but we must each do all", 57),
                        BookLine("that we can, for", 58),
                        BookLine("the greater good.", 59),
                        BookLine("<col=08088A>Year 2 Day 7", 61),
                        BookLine("I have spent my day in", 62),
                        BookLine("the pointless exercise", 63),
                        BookLine("of chasing sheep about", 64),
                        BookLine("with a big wooden device,", 65),
                    ),
                    Page(
                        BookLine("after wasting all the shots", 66),
                        BookLine("I tried putting vials of", 67),
                        BookLine("dye into it, in the end", 68),
                        BookLine("I only succeeded in", 69),
                        BookLine("breaking the device.", 70),
                        BookLine("I have been told", 71),
                        BookLine("I will be doing paper", 72),
                        BookLine("work all day tomorrow,", 73),
                        BookLine("I cannot do as much damage", 74),
                        BookLine("in the office I was told.", 75),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("<col=08088A>Year 2 Day 8", 55),
                        BookLine("Today was quite something,", 56),
                        BookLine("I got a promotion. It", 57),
                        BookLine("looks like those years", 58),
                        BookLine("of study paid off after", 59),
                        BookLine("all. I am now in charge", 60),
                        BookLine("of collating all the", 61),
                        BookLine("historical data we", 62),
                        BookLine("are collecting in the", 63),
                        BookLine("excavation site. While", 64),
                        BookLine("filing some papers I could", 65),
                    ),
                    Page(
                        BookLine("not help noticing lots", 66),
                        BookLine("of fragments from an old", 67),
                        BookLine("page, putting these pieces", 68),
                        BookLine("together revealed a message:", 69),
                        BookLine("'Seren's light will open", 70),
                        BookLine("the doors, Her crystal", 71),
                        BookLine("is but a filter'.", 72),
                        BookLine("It seems no-one else", 73),
                        BookLine("had spotted that before,", 74),
                        BookLine("I think I have earned", 75),
                        BookLine("the respect of my", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("colleagues today.", 55),
                        BookLine("<col=08088A>Year 2 Day 10", 57),
                        BookLine("For the last 2 days I", 58),
                        BookLine("have worked cataloguing", 59),
                        BookLine("all the findings, then", 60),
                        BookLine("today a messenger turned", 61),
                        BookLine("up with a document", 62),
                        BookLine("requesting that 'all", 63),
                        BookLine("materials from the", 64),
                        BookLine("excavation site should", 65),
                    ),
                    Page(
                        BookLine("be returned to Prifddinas'.", 66),
                        BookLine("I wish I could join them", 67),
                        BookLine("on that journey. I hope", 68),
                        BookLine("my brother is well. There", 69),
                        BookLine("is a rumour going around", 70),
                        BookLine("of a great human", 71),
                        BookLine("who reopened the well", 72),
                        BookLine("of voyage, and in the", 73),
                        BookLine("process of doing", 74),
                        BookLine("so banished the crazed", 75),
                        BookLine("mage. I wonder if this", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("has anything to do with", 55),
                        BookLine("the prophecies?", 56),
                        BookLine("<col=08088A>Year 2 Day 17", 58),
                        BookLine("One of the slaves escaped", 59),
                        BookLine("and ran off down the south", 60),
                        BookLine("west cavern today, he", 61),
                        BookLine("was found some hours later", 62),
                        BookLine("in a small cravasse", 63),
                        BookLine("rocking back and", 64),
                        BookLine("forth repeating", 65),
                    ),
                    Page(
                        BookLine("over and over 'No", 66),
                        BookLine("shadows get me'.", 67),
                        BookLine("It looks like I will", 68),
                        BookLine("be the one to get", 69),
                        BookLine("to go look for", 70),
                        BookLine("these shadows,", 71),
                        BookLine("I just got my orders to", 72),
                        BookLine("take a team and", 73),
                        BookLine("investigate the tunnel.", 74),
                        BookLine("I am to be briefed", 75),
                        BookLine("in the morning.", 76),
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
        on(Items.EDERNS_JOURNAL_6649, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }
    }
}
