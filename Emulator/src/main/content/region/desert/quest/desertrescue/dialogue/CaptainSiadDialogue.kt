package content.region.desert.quest.desertrescue.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import org.rs.consts.Quests

class CaptainSiadDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.THE_TOURIST_TRAP)
        when (quest!!.getStage(player)) {
            else -> {
                player.packetDispatch.sendMessage("The captain looks up from his work as you address him.")
                npc("What are you doing in here?")
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            52 ->
                when (stage) {
                    0 -> {
                        player("I wanted to have a chat?")
                        stage++
                    }

                    1 -> {
                        npc("You don't belong in here, get out!")
                        stage++
                    }

                    2 -> {
                        player("You seem to have a lot of books!")
                        stage++
                    }

                    3 -> {
                        npc("Yes, I do. Now please get to the point?")
                        stage++
                    }

                    4 -> {
                        player("So, you're interested in sailing?")
                        stage++
                    }

                    5 -> {
                        interpreter.sendDialogue("The captain's interest seems to perk up.")
                        stage++
                    }

                    6 -> {
                        npc("Well, yes actually... It's been a passion of mine for", "some years...")
                        stage++
                    }

                    7 -> {
                        player("I could tell by the cut of your jib.")
                        stage++
                    }

                    8 -> {
                        npc("Oh yes? Really?")
                        stage++
                    }

                    9 -> {
                        npc("Well, I was quite a catch in my day you know!")
                        stage++
                    }

                    10 -> {
                        interpreter.sendDialogue(
                            "The captain starts rambling on about his days as a salty sea dog. He",
                            "looks quite distracted...",
                        )
                        stage++
                    }

                    11 -> {
                        end()
                        quest!!.setStage(player, 53)
                    }
                }

            else ->
                when (stage) {
                    0 -> {
                        player("I wanted to have a chat?")
                        stage++
                    }

                    1 -> {
                        npc("You don't belong in here, get out!")
                        stage++
                    }

                    2 -> end()
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(831)
    }
}
