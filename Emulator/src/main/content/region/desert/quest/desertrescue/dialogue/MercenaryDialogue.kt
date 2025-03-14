package content.region.desert.quest.desertrescue.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.Quests

class MercenaryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (quest!!.getStage(player)) {
            10 -> npc("Yeah, what do you want?")
            100 -> npc("What are you looking at?")
            else -> npc("What are you doing here?")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            10 ->
                when (stage) {
                    0 -> {
                        player("I'm looking for a woman called Ana, have you seen her?")
                        stage++
                    }

                    1 -> {
                        npc("No, now get lost!")
                        stage++
                    }

                    2 -> {
                        player("Perhaps five gold coins will help you remember?")
                        stage++
                    }

                    3 -> {
                        npc("Well, it certainly will help!")
                        stage = 4
                    }

                    4 -> {
                        if (!player.inventory.containsItem(COINS)) {
                            player("Sorry, I don't seem to have enough coins.")
                            stage++
                        }
                        if (player.inventory.remove(COINS)) {
                            interpreter.sendItemMessage(COINS, "-- The guard takes the five gold coins. --")
                            stage = 6
                        }
                    }

                    5 -> end()
                    6 -> {
                        npc("Now then, what did you want to know?")
                        stage++
                    }

                    7 -> {
                        player("I'm looking for a woman called Ana, have you seen her?")
                        stage++
                    }

                    8 -> {
                        npc(
                            "Hmm, well, we get a lot of people in here. But not",
                            "many women through... Saw one come in last week....",
                        )
                        stage++
                    }

                    9 -> {
                        npc("But I don't know if it's the woman you're looking for?")
                        stage++
                    }

                    10 -> {
                        player("What are you guarding?")
                        stage = 11
                    }

                    11 -> {
                        npc(
                            "Well, if you have to know, we're making sure that no",
                            "prisoners get out. <col=08088A>-- The guard gives you a",
                            "<col=08088A>disapproving look. -- </col>And to make sure that",
                            "unauthorised people don't get in.",
                        )
                        stage = 12
                    }

                    12 -> {
                        npc(
                            "<col=08088A>-- The guard looks around nervously. --</col> You'd better",
                            "go soon before the Captain orders us to kill you.",
                        )
                        stage++
                    }

                    13 -> {
                        player("Does the Captain order you to kill a lot of people?")
                        stage++
                    }

                    14 -> {
                        npc("<col=08088A>-- The guard snorts. --</col> *Snort* Just about anyone", "who talks to him.")
                        stage++
                    }

                    15 -> {
                        npc(
                            "Unless he has a use for you, he'll probably just order",
                            "us to kill you. And it's such a horrible job cleaning up",
                            "the mess afterwards.",
                        )
                        stage++
                    }

                    16 -> {
                        player("Not to mention the senseless waste of human life.")
                        stage++
                    }

                    17 -> {
                        npc("Huh? Them's your words, not mine.")
                        stage++
                    }

                    18 -> {
                        player("It doesn't sound as if you respect your Captain much.")
                        stage++
                    }

                    19 -> {
                        interpreter.sendDialogue("-- The guard looks around conspiratorially. --")
                        stage++
                    }

                    20 -> {
                        npc(
                            "Well, to be honest. We think he's not exactly as brave",
                            "as he makes out. But we have to follow his orders. If",
                            "someone called him a coward, or managed to trick him",
                            "into a one-on-one duel many of us bet that he'd be",
                        )
                        stage++
                    }

                    21 -> {
                        npc("beaten.")
                        stage++
                    }

                    22 -> {
                        player("And how could I trick him into a one-on-one duel?")
                        stage++
                    }

                    23 -> {
                        npc(
                            "Like all cowards, he likes to be made to feel important.",
                            "If anyone insults him outright, he just gets us to do his",
                            "dirty work. However, if he thinks he's better than you,",
                            "if you compliment him, he may feel that he can defeat",
                        )
                        stage = 24
                    }

                    24 -> {
                        npc(
                            "you. And if he initiated a duel, all the men agreed that",
                            "they wouldn't intervene. We think he'd be slaughtered",
                            "in double quick time.",
                        )
                        stage++
                    }

                    25 -> {
                        quest!!.setStage(player, 11)
                        end()
                    }
                }

            100 -> end()
            else ->
                when (stage) {
                    0 -> {
                        player("Nothing, just passing by.")
                        stage++
                    }

                    1 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(4989, 4990, 4991, 4992)
    }

    companion object {
        private val COINS = Item(995, 5)
    }
}
