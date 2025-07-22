package content.global.plugin.item.book.diary

import content.global.plugin.iface.BookInterface
import content.global.plugin.iface.BookLine
import content.global.plugin.iface.Page
import content.global.plugin.iface.PageSet
import content.region.karamja.brimhaven.plugin.BrimhavenPlugin.Companion.success
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery

class LockedDiary : InteractionListener {
    /*
     * Found by searching Sandy's desk in Brimhaven during
     * the Body of Clarence miniquest after the Back to my Roots quest.
     * Sources: https://youtu.be/WBtwZhnibJA?si=2VfUMB6R4b1zwCCw&t=458
     * Authentic state.
     */

    companion object {
        private const val TITLE = "Sandy's Diary"
        private val CONTENTS =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("4th Bennath, 168", 55),
                        BookLine("I'm going to start keeping", 56),
                        BookLine("a diary of my life, to", 57),
                        BookLine("leave to all those budding", 58),
                        BookLine("entrepreneurs who want", 59),
                        BookLine("to start their own", 60),
                        BookLine("business - who knows,", 61),
                        BookLine("maybe in the future", 62),
                        BookLine("someone can write a book", 63),
                        BookLine("about my success.", 64),
                    ),
                    Page(
                        BookLine("Anyway, today I hit on a", 66),
                        BookLine("really great idea. People", 67),
                        BookLine("are always running out", 68),
                        BookLine("of sand in the sand pits,", 69),
                        BookLine("so I'm going to set up a", 70),
                        BookLine("business carting sand", 71),
                        BookLine("from some sunny beach", 72),
                        BookLine("all the way to the", 73),
                        BookLine("sandpits. This also means", 74),
                        BookLine("I get a free holiday", 75),
                        BookLine("every day. I think it's a", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("rather good idea.", 55),
                    ),
                    Page(
                        BookLine("5th Bennath, 168", 66),
                        BookLine("", 67),
                        BookLine("Spent my day drawing", 68),
                        BookLine("out plans for my", 69),
                        BookLine("business, it looks like", 70),
                        BookLine("Karamja will be my best", 71),
                        BookLine("bet, especially since", 72),
                        BookLine("there's already a", 73),
                        BookLine("settlement there, the", 74),
                        BookLine("natives shouldn't bother", 75),
                        BookLine("me that much, and I can", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("hire some people to take", 55),
                        BookLine("sand via the boats. Had a", 56),
                        BookLine("nice fillet of bass for tea.", 57),
                    ),
                    Page(
                        BookLine("12th Bennath, 168", 66),
                        BookLine("", 67),
                        BookLine("I haven't written here for", 68),
                        BookLine("a while. After making", 69),
                        BookLine("some enquiries about an", 70),
                        BookLine("office on Karamja for a", 71),
                        BookLine("new business, things fairly", 72),
                        BookLine("raced ahead. I need to", 73),
                        BookLine("sort out some way of", 74),
                        BookLine("getting the building set", 75),
                        BookLine("up now: perhaps I can", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("hire some of the local", 55),
                        BookLine("natives to build it for me?", 56),
                    ),
                    Page(
                        BookLine("6th Raktuber, 168", 66),
                        BookLine("", 67),
                        BookLine("Okay, so my daily diary", 68),
                        BookLine("has gone a bit awry, but", 69),
                        BookLine("the building is finally", 70),
                        BookLine("done. I hung the sign", 71),
                        BookLine("today myself. Now", 72),
                        BookLine("I need to get things", 73),
                        BookLine("running quickly - the", 74),
                        BookLine("loan I got from that man", 75),
                        BookLine("in the pub needs", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("repaying...and I have a", 55),
                        BookLine("feeling that I had better", 56),
                        BookLine("not default on it. I have", 57),
                        BookLine("to get things in order", 58),
                        BookLine("and find some customers.", 59),
                        BookLine("I wish I'd done this", 60),
                        BookLine("before building the", 61),
                        BookLine("headquarters. Still, the", 62),
                        BookLine("weather is beautiful and I", 63),
                        BookLine("shall sail to Port Khazard", 64),
                        BookLine("today.", 65),
                    ),
                    Page(
                        BookLine("15th Raktuber, 168", 66),
                        BookLine("", 67),
                        BookLine("I think I have my first", 68),
                        BookLine("customer. Good job, too,", 69),
                        BookLine("as that man who gave me", 70),
                        BookLine("the loan came round", 71),
                        BookLine("yesterday with some", 72),
                        BookLine("friends of his. At first I", 73),
                        BookLine("thought they were a", 74),
                        BookLine("charity as they all had", 75),
                        BookLine("black bands on their", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("arms... I must find some", 55),
                        BookLine("money for tomorrow.", 56),
                        BookLine("Luckily, that doesn't seem", 57),
                        BookLine("too hard as my customer", 58),
                        BookLine("in Yanille seems to be", 59),
                        BookLine("willing to pay for the", 60),
                        BookLine("sand, and even threw in", 61),
                        BookLine("some labour to help", 62),
                        BookLine("transport it. Mind you,", 63),
                        BookLine("I'm not sure the old guy", 64),
                        BookLine("can actually carry the", 65),
                    ),
                    Page(
                        BookLine("sand, but we'll see.", 66),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("16th Raktuber, 168", 55),
                        BookLine("", 56),
                        BookLine("Got my first payment for", 57),
                        BookLine("sand today, not bad if I", 58),
                        BookLine("do say so myself. Only", 59),
                        BookLine("problem is, they came", 60),
                        BookLine("round for the loan", 61),
                        BookLine("payment today. So yet", 62),
                        BookLine("again I'm left with", 63),
                        BookLine("nothing.", 64),
                    ),
                    Page(
                        BookLine("30th Raktuber, 168", 66),
                        BookLine("", 67),
                        BookLine("I've managed to get", 68),
                        BookLine("another customer, this", 69),
                        BookLine("time on that pacifist island", 70),
                        BookLine("of Entrana. Not sure", 71),
                        BookLine("what they need all the", 72),
                        BookLine("sand for, but they're", 73),
                        BookLine("paying well and I", 74),
                        BookLine("managed to hire someone", 75),
                        BookLine("to take the sand there,", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("too. Things aren't looking", 55),
                        BookLine("too bad. Just had a word", 56),
                        BookLine("with the sand shifter from", 57),
                        BookLine("Yanille, his name is Bert", 58),
                        BookLine("and he's slow. Maybe", 59),
                        BookLine("there's some way I can", 60),
                        BookLine("get him to go faster. Pity", 61),
                        BookLine("I can't fire him but he", 62),
                        BookLine("comes with the contract", 63),
                        BookLine("from Yanille. Oh, well.", 64),
                        BookLine("Had a rather nice lobster", 65),
                    ),
                    Page(
                        BookLine("for tea, going for a stroll", 66),
                        BookLine("along the beach.", 67),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("25th Fentuary, 168", 55),
                        BookLine("", 56),
                        BookLine("I haven't written here for", 57),
                        BookLine("a while, I was too busy", 58),
                        BookLine("with paperwork. Seem to", 59),
                        BookLine("have another customer up", 60),
                        BookLine("in the <str>Fremen Freminne</str>", 61),
                        BookLine("<str>Freminick</str> Fremmy", 62),
                        BookLine("Province. Also managed", 63),
                        BookLine("to hire a pleasant chap", 64),
                        BookLine("from there who can ship", 65),
                    ),
                    Page(
                        BookLine("the sand. The loan is", 66),
                        BookLine("nearly paid off, but I", 67),
                        BookLine("need to pay the workers", 68),
                        BookLine("as at the moment they're", 69),
                        BookLine("being quite good about", 70),
                        BookLine("the whole thing. I think", 71),
                        BookLine("today I will go have", 72),
                        BookLine("dinner at the pub.", 73),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("14th Septober, 168", 55),
                        BookLine("", 56),
                        BookLine("Loan paid off. Workers", 57),
                        BookLine("getting shirty about pay.", 58),
                        BookLine("Up to my neck in", 59),
                        BookLine("paperwork and Postie", 60),
                        BookLine("Pete delivered a letter", 61),
                        BookLine("from the tax man today.", 62),
                        BookLine("Things not looking good.", 63),
                        BookLine("Going back to bed as it's", 64),
                        BookLine("raining outside.", 65),
                    ),
                    Page(
                        BookLine("15th Septober, 168", 66),
                        BookLine("", 67),
                        BookLine("The rain still keeps", 68),
                        BookLine("coming, as do the", 69),
                        BookLine("demands for money.", 70),
                        BookLine("Luckily, I think I", 71),
                        BookLine("managed to balance the", 72),
                        BookLine("books, and the payments", 73),
                        BookLine("just came in for the", 74),
                        BookLine("Yanille and Entrana pits.", 75),
                        BookLine("I have enough to pay the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("workers now. I wish", 55),
                        BookLine("they'd work longer for", 56),
                        BookLine("less! Getting a bit sick of", 57),
                        BookLine("fish for tea. Might see if", 58),
                        BookLine("I can borrow some of the", 59),
                        BookLine("bananas that seem so", 60),
                        BookLine("abundant around here.", 61),
                    ),
                    Page(
                        BookLine("12th Ire of Phyrrys, 168", 66),
                        BookLine("", 67),
                        BookLine("Today, while staring out", 68),
                        BookLine("into the rain, I had an", 69),
                        BookLine("idea. I am going to see if", 70),
                        BookLine("I can get a wizard to", 71),
                        BookLine("enchant my workers to", 72),
                        BookLine("work harder for less. A", 73),
                        BookLine("simple mind spell should", 74),
                        BookLine("work, depends how much", 75),
                        BookLine("it will cost, though. Almost", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("got caught stealing", 55),
                        BookLine("bananas yesterday but", 56),
                        BookLine("think I got away with it.", 57),
                    ),
                    Page(
                        BookLine("18th Ire of Phyrrys, 168", 66),
                        BookLine("", 67),
                        BookLine("Passage booked on the", 68),
                        BookLine("boat; I'm going back to", 69),
                        BookLine("Yanille to see if I can", 70),
                        BookLine("hire a wizard. Of course,", 71),
                        BookLine("it will have to be one of", 72),
                        BookLine("the more gullible ones,", 73),
                        BookLine("them being all honest and", 74),
                        BookLine("benevolent just doesn't", 75),
                        BookLine("help when a businessman", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("needs a simple spell.", 55),
                        BookLine("Write later, sun just", 56),
                        BookLine("came out so I'll go see", 57),
                        BookLine("what I can catch to eat.", 58),
                    ),
                    Page(
                        BookLine("2nd Novtumber, 168", 66),
                        BookLine("", 67),
                        BookLine("I left the business in a", 68),
                        BookLine("good state, able to run", 69),
                        BookLine("itself until mid", 70),
                        BookLine("Novtumber. I think I'm", 71),
                        BookLine("making progress with a", 72),
                        BookLine("wizard I've met in the", 73),
                        BookLine("Dragon Inn where I'm", 74),
                        BookLine("staying in Yanille. It", 75),
                        BookLine("seems he's down on his", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("luck and a bit new to the", 55),
                        BookLine("whole wizard thing. I", 56),
                        BookLine("talked to him about the", 57),
                        BookLine("spell I want this evening.", 58),
                        BookLine("I'm hoping he'll think that", 59),
                        BookLine("this kind of thing is a", 60),
                        BookLine("regular occurrence.", 61),
                    ),
                    Page(
                        BookLine("5th Novtumber, 168", 66),
                        BookLine("", 67),
                        BookLine("I wish I was back on", 68),
                        BookLine("Karamja. It's cold here.", 69),
                        BookLine("But my gullible wizard", 70),
                        BookLine("friend, Clarence, has", 71),
                        BookLine("started on the spell I", 72),
                        BookLine("need. I'm going to try it", 73),
                        BookLine("on Bert, the Yanille", 74),
                        BookLine("sandpit worker before I", 75),
                        BookLine("roll it out to the whole lot", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("of them. Basically, all it", 55),
                        BookLine("does is make him think", 56),
                        BookLine("that it's perfectly normal", 57),
                        BookLine("to be working hugely long", 58),
                        BookLine("hours for very little pay.", 59),
                        BookLine("I'm not quite sure how it", 60),
                        BookLine("works, but I need to", 61),
                        BookLine("work out a new rota for", 62),
                        BookLine("Bert. This should ease", 63),
                        BookLine("the situation with", 64),
                        BookLine("outgoings and incomings,", 65),
                    ),
                    Page(
                        BookLine("maybe I'll have enough", 66),
                        BookLine("money so I don't need to", 67),
                        BookLine("eat fish constantly.", 68),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("14th Novtumber, 168", 55),
                        BookLine("", 56),
                        BookLine("Back on Karamja. It's", 57),
                        BookLine("warmer here, but still", 58),
                        BookLine("quite cold as winter closes", 59),
                        BookLine("in. The spell seems to be", 60),
                        BookLine("working and was well", 61),
                        BookLine("worth the amount I paid", 62),
                        BookLine("for it. I will trial it for", 63),
                        BookLine("the next three months,", 64),
                        BookLine("then see if I can get", 65),
                    ),
                    Page(
                        BookLine("Clarence to do the same", 66),
                        BookLine("for the other workers.", 67),
                        BookLine("Trout for supper. Urg.", 68),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("1st Moevyng, 169", 55),
                        BookLine("", 56),
                        BookLine("Winter seems to be", 57),
                        BookLine("coming to a close, thank", 58),
                        BookLine("the gods. Profits are up -", 59),
                        BookLine("in fact, they're soaring", 60),
                        BookLine("and Yanille is the most", 61),
                        BookLine("profitable sandpit, but with", 62),
                        BookLine("the oldest worker. I am", 63),
                        BookLine("going to see if I can get", 64),
                        BookLine("Clarence to do the same", 65),
                    ),
                    Page(
                        BookLine("for the Entrana and", 66),
                        BookLine("<str>Freminne Fremen</str>", 67),
                        BookLine("<str>Freminick</str> Fremm", 68),
                        BookLine("sandpits, too.", 69),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("5th Moevyng, 169", 55),
                        BookLine("", 56),
                        BookLine("Received a letter from", 57),
                        BookLine("Clarence. He appears to", 58),
                        BookLine("be getting a little nervous", 59),
                        BookLine("about that spell that he", 60),
                        BookLine("did for me. I don't think", 61),
                        BookLine("I should ask him to do", 62),
                        BookLine("the other two now. Must", 63),
                        BookLine("convince him that it's all", 64),
                        BookLine("perfectly normal. Can't", 65),
                    ),
                    Page(
                        BookLine("lose these profits. I", 66),
                        BookLine("managed to get a baked", 67),
                        BookLine("potato for lunch...it was", 68),
                        BookLine("divine after so much fish.", 69),
                        BookLine("How do these people live", 70),
                        BookLine("on fish all the time?", 71),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("30th Moevyng, 169", 55),
                        BookLine("", 56),
                        BookLine("Have decided that", 57),
                        BookLine("something needs to be", 58),
                        BookLine("done about Clarence, he's", 59),
                        BookLine("started vague threats in", 60),
                        BookLine("his letters about telling", 61),
                        BookLine("the authorities. Have", 62),
                        BookLine("invited him out here for a", 63),
                        BookLine("holiday... I'll see if I can", 64),
                        BookLine("convince him that", 65),
                    ),
                    Page(
                        BookLine("everything is fine...but I", 66),
                        BookLine("won't lose these profits,", 67),
                        BookLine("no matter what.", 68),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("15th Bennath, 169", 55),
                        BookLine("", 56),
                        BookLine("Pay day for the workers", 57),
                        BookLine("and pay day for me!", 58),
                        BookLine("More money! It's all", 59),
                        BookLine("working nicely. Clarence", 60),
                        BookLine("is due to arrive today, so", 61),
                        BookLine("I must pick him up at the", 62),
                        BookLine("dock. Hopefully I can", 63),
                        BookLine("find something better for", 64),
                        BookLine("lunch than trout.", 65),
                    ),
                    Page(
                        BookLine("16th Bennath, 169", 66),
                        BookLine("", 67),
                        BookLine("It's becoming apparent", 68),
                        BookLine("that Clarence really is", 69),
                        BookLine("quite the lawful one, and", 70),
                        BookLine("has been reading up on", 71),
                        BookLine("his wizard guild", 72),
                        BookLine("regulations. This could", 73),
                        BookLine("cause me some problems.", 74),
                        BookLine("I did tell him that this", 75),
                        BookLine("holiday may be a long", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("one, though, so his guild", 55),
                        BookLine("isn't expecting him back", 56),
                        BookLine("for a while. I'll see how", 57),
                        BookLine("long I can keep him here,", 58),
                        BookLine("but if he gets difficult,", 59),
                        BookLine("I'm not sure what I'll do.", 60),
                        BookLine("Feed him more fish, I", 61),
                        BookLine("guess.", 62),
                    ),
                    Page(
                        BookLine("30th Bennath, 169", 66),
                        BookLine("", 67),
                        BookLine("I'm not sure that I", 68),
                        BookLine("should have solved the", 69),
                        BookLine("problem of Clarence. I", 70),
                        BookLine("have to find someway of", 71),
                        BookLine("getting rid of the bits", 72),
                        BookLine("now. At least my profits", 73),
                        BookLine("from the Yanille sandpit", 74),
                        BookLine("are really soaring.", 75),
                        BookLine("Hmm...that gives me an", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("idea.", 55),
                    ),
                    Page(
                        BookLine("3rd Raktuber, 169", 66),
                        BookLine("", 67),
                        BookLine("Keep having to remind", 68),
                        BookLine("myself that it's for the", 69),
                        BookLine("good of the business.", 70),
                        BookLine("Profit still rolling in.", 71),
                        BookLine("Problem is, I keep", 72),
                        BookLine("jumping out of my skin", 73),
                        BookLine("every time someone", 74),
                        BookLine("comes in the office. Still,", 75),
                        BookLine("I've nearly disposed of all", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("the evidence now. Quite", 55),
                        BookLine("ironic that some of him", 56),
                        BookLine("went back to Yanille in", 57),
                        BookLine("the last shipment! Thank", 58),
                        BookLine("the gods for that huge", 59),
                        BookLine("vine growing down near", 60),
                        BookLine("Shilo. Guess what... Fish", 61),
                        BookLine("for tea.", 62),
                    ),
                    Page(
                        BookLine("33rd Raktuber, 169", 66),
                        BookLine("", 67),
                        BookLine("Had a nosy adventurer", 68),
                        BookLine("around today. I think I", 69),
                        BookLine("put them off. Hopefully I", 70),
                        BookLine("won't see the likes of", 71),
                        BookLine("them again. Put me back", 72),
                        BookLine("on edge when I was just", 73),
                        BookLine("beginning to relax. Must", 74),
                        BookLine("find another wizard to", 75),
                        BookLine("cast a similar spell on the", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("other two workers as I", 55),
                        BookLine("want more profit in next", 56),
                        BookLine("month.", 57),
                    ),
                    Page(
                        BookLine("3rd Pentember, 169", 66),
                        BookLine("", 67),
                        BookLine("I think I just got tricked.", 68),
                        BookLine("I was distracted for a", 69),
                        BookLine("moment when I had", 70),
                        BookLine("someone asking me", 71),
                        BookLine("questions and...it all seems", 72),
                        BookLine("a bit hazy...sounds like", 73),
                        BookLine("there's someone coming,", 74),
                        BookLine("I'll finish off this entry", 75),
                        BookLine("tomo-", 76),
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
        sendString(
            player,
            "",
            BookInterface.FANCY_BOOK_3_49,
            BookInterface.FANCY_BOOK_3_49_LINE_IDS[1]
        )
        sendString(
            player,
            "",
            BookInterface.FANCY_BOOK_3_49,
            BookInterface.FANCY_BOOK_3_49_LINE_IDS[2]
        )
        return true
    }

    override fun defineListeners() {

        /*
         * Handles read the sandy diaries.
         */

        on(Items.UNLOCKED_DIARY_11762, IntType.ITEM, "read") { player, _ ->
            BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
            return@on true
        }

        /*
         * Handles unlock the possibility to read the book.
         */

        on(Items.LOCKED_DIARY_11761, IntType.ITEM, "unlock") { player, _ ->
            val success = success(player, Skills.THIEVING)
            if (removeItem(player, Item(Items.LOCKED_DIARY_11761, 1))) {
                if (success) {
                    sendMessage(player, "You carefully unlock the diary.")
                    addItemOrDrop(player, Items.UNLOCKED_DIARY_11762, 1)
                    BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::display)
                } else {
                    sendMessage(player, "You fail to open the diary.")
                    val damage = (getDynLevel(player, Skills.HITPOINTS) * 0.5).toInt()
                    impact(player, damage, ImpactHandler.HitsplatType.NORMAL)
                }
            }
            return@on true
        }

        /*
         * Handles retrieve locked diary.
         */

        on(Scenery.SANDY_S_DESK_10805, IntType.SCENERY, "search") { player, _ ->
            player.animate(Animation(Animations.MULTI_TAKE_832))
            sendMessage(
                player,
                "You search through the papers on the table and find a locked diary."
            )
            addItemOrDrop(player, Items.LOCKED_DIARY_11761, 1)
            return@on true
        }
    }
}
