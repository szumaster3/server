package content.region.fremennik.quest.royal.handlers

import content.global.handlers.iface.BookInterface
import content.global.handlers.iface.BookLine
import content.global.handlers.iface.Page
import content.global.handlers.iface.PageSet
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.DARK_RED

class BurntDiary : InteractionListener {
    companion object {
        private val DIARY_PAGES = intArrayOf(7961, 7962, 7963, 7964)
        private val TITLE = "Armod's Burnt Diary"

        private val PAGE_ONE =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
            )

        private val PAGE_TWO =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
            )

        private val PAGE_THREE =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Year 5 of trials, day 93", 55),
                        BookLine("", 56),
                        BookLine("Life's not fair!", 57),
                        BookLine("We got to the island", 58),
                        BookLine("kingdoms and they said", 59),
                        BookLine("that an adventurer had", 60),
                        BookLine("recently made peace, by", 61),
                        BookLine("promising to marry the", 62),
                        BookLine("son of the king!", 63),
                        BookLine("Why does this always", 64),
                        BookLine("happen to us? If we'd", 65),
                    ),
                    Page(
                        BookLine("just been a little earlier, I", 66),
                        BookLine("could have married the", 67),
                        BookLine("king's daughter.", 68),
                        BookLine("", 69),
                        BookLine("Year 5 of trials, day 95", 70),
                        BookLine("", 71),
                        BookLine("Beigarth says that just", 72),
                        BookLine("because they're at peace", 73),
                        BookLine("now doesn't mean they", 74),
                        BookLine("always will. He says he", 75),
                        BookLine("has a plan.", 76),
                    ),
                ),
            )

        private val PAGE_FOUR =
            arrayOf(
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Property of Armod", 55),
                        BookLine("Brundtson", 56),
                        BookLine("", 57),
                        BookLine("${DARK_RED}Read at your peril!", 58),
                        BookLine("", 59),
                        BookLine("If you steal this book, my", 60),
                        BookLine("dad will beat you up!", 61),
                    ),
                    Page(
                        BookLine("Year 3 of trials, day 20", 66),
                        BookLine("", 67),
                        BookLine("We're doing really badly", 68),
                        BookLine("at the trials. Beigarth", 69),
                        BookLine("broke his lyre for the fifth", 70),
                        BookLine("time, Hild got lost in the", 71),
                        BookLine("maze again, and Reinn", 72),
                        BookLine("and Signy were both sick", 73),
                        BookLine("all over Manni. When", 74),
                        BookLine("are we going to finish", 75),
                        BookLine("these trials and become", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("adults? My father keeps", 55),
                        BookLine("telling me to improve,", 56),
                        BookLine("saying that I should try", 57),
                        BookLine("harder to become a", 58),
                        BookLine("proper Fremennik adult.", 59),
                        BookLine("", 60),
                        BookLine("Year 4 of trials, day 144", 61),
                        BookLine("", 62),
                        BookLine("The trials aren't going", 63),
                        BookLine("well. I broke my arm while", 64),
                        BookLine("fighting a cow, and", 65),
                    ),
                    Page(
                        BookLine("Beigarth almost fell into", 66),
                        BookLine("the furnace. Luckily, my", 67),
                        BookLine("father saved him and", 68),
                        BookLine("Signy from spilling molten", 69),
                        BookLine("iron all over the adults.", 70),
                        BookLine("Askeladden's already", 71),
                        BookLine("become an adult and", 72),
                        BookLine("keeps making fun of us.", 73),
                        BookLine("He really annoys me.", 74),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("Year 5 of trials, day 93", 55),
                        BookLine("", 56),
                        BookLine("Life's not fair!", 57),
                        BookLine("We got to the island", 58),
                        BookLine("kingdoms and they said", 59),
                        BookLine("that an adventurer had", 60),
                        BookLine("recently made peace, by", 61),
                        BookLine("promising to marry the", 62),
                        BookLine("son of the king!", 63),
                        BookLine("Why does this always", 64),
                        BookLine("happen to us? If we'd", 65),
                    ),
                    Page(
                        BookLine("just been a little earlier, I", 66),
                        BookLine("could have married the", 67),
                        BookLine("king's daughter.", 68),
                        BookLine("", 69),
                        BookLine("Year 5 of trials, day 95", 70),
                        BookLine("", 71),
                        BookLine("Beigarth says that just", 72),
                        BookLine("because they're at peace", 73),
                        BookLine("now doesn't mean they", 74),
                        BookLine("always will. He says he", 75),
                        BookLine("has a plan.", 76),
                    ),
                ),
                PageSet(
                    Page(
                        BookLine("", 55),
                        BookLine("Year 5 of trials, day 95", 56),
                        BookLine("", 57),
                        BookLine("Beigarth's plan is great!", 58),
                        BookLine("Now all we have to do is", 59),
                        BookLine("find the right armour,", 60),
                        BookLine("and a place in one of the", 61),
                        BookLine("caves. Father's going to", 62),
                        BookLine("be so impressed with me...", 63),
                    ),
                    Page(
                        BookLine("Year 5 of trials, day 98", 66),
                        BookLine("", 67),
                        BookLine("The plan's worked! The", 68),
                        BookLine("kingdoms are at war", 69),
                        BookLine("again, and no-one knows", 70),
                        BookLine("we were those soldiers. All", 71),
                        BookLine("we need to do is make", 72),
                        BookLine("peace, and the council at", 73),
                        BookLine("home will be so impressed", 74),
                        BookLine("that they'll make us adults", 75),
                        BookLine("right away!", 76),
                    ),
                ),
            )
    }

    private fun pageOne(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, PAGE_ONE)
        return true
    }

    private fun pageTwo(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, PAGE_TWO)
        return true
    }

    private fun pageThree(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, PAGE_THREE)
        return true
    }

    private fun pageFour(
        player: Player,
        pageNum: Int,
        buttonID: Int,
    ): Boolean {
        BookInterface.pageSetup(player, BookInterface.FANCY_BOOK_3_49, TITLE, PAGE_FOUR)
        return true
    }

    override fun defineListeners() {
        on(DIARY_PAGES, IntType.ITEM, "read") { player, node ->
            when (node.id) {
                7961 -> BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::pageOne)
                7962 -> BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::pageTwo)
                7963 -> BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::pageThree)
                else -> BookInterface.openBook(player, BookInterface.FANCY_BOOK_3_49, ::pageFour)
            }
            return@on true
        }
    }
}
