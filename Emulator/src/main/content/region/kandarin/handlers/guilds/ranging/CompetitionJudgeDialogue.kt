package content.region.kandarin.handlers.guilds.ranging

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Competition judge dialogue.
 */
@Initializable
class CompetitionJudgeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (player.inventory.getAmount(Items.ARCHERY_TICKET_1464) >= 1000 &&
            !hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, 1, 7)
        ) {
            npc("Wow! I see that you've got yourself a whole load of ", "archery tickets. Well done!")
            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 1, 7)
            stage = -1
        } else if (player.archeryTargets > 0) {
            npc("Hello again, do you need reminding of the rules?")
            stage = 20
        } else if (player.archeryTotal == 0) {
            npc(
                "Hello there, would you like to take part in the",
                "archery competition? It only costs 200 coins to",
                "enter.",
            )
            stage = 0
        } else {
            val reward = player.archeryTotal / 10
            npc(
                "Well done. Your score is: " + player.archeryTotal + ".",
                "For that score you will receive $reward Archery tickets.",
            )
            player.archeryTargets = -1
            player.archeryTotal = 0
            if (!player.inventory.add(Item(Items.ARCHERY_TICKET_1464, reward))) {
                player.bank.add(Item(Items.ARCHERY_TICKET_1464, reward))
                player.sendMessage("Your reward was sent to your bank.")
            }
            stage = 999
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            999 -> end()
            -1 ->
                if (player.archeryTargets > 0) {
                    npc("Hello again, do you need reminding of the rules?")
                    stage = 20
                } else if (player.archeryTotal == 0) {
                    npc(
                        "Hello there, would you like to take part in the",
                        "archery competition? It only costs 200 coins to",
                        "enter.",
                    )
                    stage = 0
                } else {
                    val reward = player.archeryTotal / 10
                    npc(
                        "Well done. Your score is: " + player.archeryTotal + ".",
                        "For that score you will receive $reward Archery tickets.",
                    )
                    if (!player.inventory.add(Item(Items.ARCHERY_TICKET_1464, reward))) {
                        player.bank.add(Item(Items.ARCHERY_TICKET_1464, reward))
                        player.sendMessage("Your reward was sent to your bank.")
                    }
                    stage = 999
                }

            0 -> {
                options("Sure, I'll give it a go.", "What are the rules?", "No thanks.")
                stage++
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("Sure, I'll give it a go.")
                        stage = 2
                    }

                    2 -> {
                        player("What are the rules?")
                        stage = 5
                    }

                    3 -> {
                        player("No thanks.")
                        stage = 999
                    }
                }

            2 -> {
                npc("Great! That will be 200 coins then please.")
                stage++
            }

            3 ->
                if (amountInInventory(player, Items.COINS_995) < 200) {
                    player("Oops, I don't have enough coins on me...")
                    stage++
                } else {
                    end()
                    sendMessage(player, "You pay the judge and he gives you 10 bronze arrows.")
                    removeItem(player, Item(Items.COINS_995, 200))
                    addItem(player, Items.BRONZE_ARROW_882, 10)
                    player.archeryTargets = 10
                    player.archeryTotal = 0
                }

            4 -> {
                npc("Never mind, come back when you've got enough.")
                stage = 999
            }

            5, 22 -> {
                npc("The rules are very simple:")
                stage++
            }

            6, 23 -> {
                npc(
                    "You're given 10 shots at the targets, for each hit",
                    "you will receive points. At the end you'll be",
                    "rewarded 1 ticket for every 10 points.",
                )
                stage++
            }

            7 -> {
                npc(
                    "The tickets can be exchanged for goods from our stores.",
                    "Do you want to give it a go? Only 200 coins.",
                )
                stage++
            }

            8 -> {
                options("Sure, I'll give it a go.", "No thanks.")
                stage++
            }

            9 ->
                when (buttonId) {
                    1 -> {
                        player("Sure, I'll give it a go.")
                        stage = 2
                    }

                    3 -> {
                        player("No thanks.")
                        stage = 999
                    }
                }

            20 -> {
                val arrows = (
                    player.inventory.getAmount(Items.BRONZE_ARROW_882) +
                        player.equipment.getAmount(Items.BRONZE_ARROW_882)
                )
                if (arrows < 1) {
                    player("Well, I actually don't have any more arrows. Could I", "get some more?")
                    stage = 25
                } else {
                    options("Yes please.", "No thanks, I've got it.", "How am I doing so far?")
                    stage++
                }
            }

            21 ->
                when (buttonId) {
                    1 -> {
                        player("Yes please.")
                        stage++
                    }

                    2 -> {
                        player("No thanks, I've got it.")
                        stage = 30
                    }

                    3 -> {
                        player("How am I doing so far?")
                        stage = 40
                    }
                }

            24 -> {
                npc("The tickets can be exchanged for goods from our stores.", "Good Luck!")
                stage = 999
            }

            25 -> {
                npc("Ok, but it'll cost you 100 coins.")
                stage++
            }

            26 -> {
                options("Sure, I'll take some.", "No thanks.")
                stage++
            }

            27 ->
                when (buttonId) {
                    1 -> {
                        player("Sure, I'll take some.")
                        stage++
                    }

                    2 -> {
                        player("No thanks.")
                        stage = 999
                    }
                }

            28 ->
                if (player.inventory.getAmount(995) < 100) {
                    player("Oops, I don't have enough coins on me...")
                    stage++
                } else {
                    end()
                    player.packetDispatch.sendMessage("You pay the judge and he gives you 10 bronze arrows.")
                    player.inventory.remove(Item(995, 100))
                    player.inventory.add(Item(882, 10))
                }

            30 -> {
                npc("Glad to hear it, good luck!")
                stage = 999
            }

            40 -> {
                val msg =
                    if (player.archeryTotal <= 0) {
                        "You haven't started yet."
                    } else if (player.archeryTotal <= 80) {
                        "Not bad, keep going."
                    } else {
                        "You're pretty good, keep it up."
                    }
                npc("So far your score is: " + player.archeryTotal, msg)
                stage = 999
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COMPETITION_JUDGE_693)
    }
}
