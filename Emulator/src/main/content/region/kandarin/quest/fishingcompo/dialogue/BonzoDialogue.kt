package content.region.kandarin.quest.fishingcompo.dialogue

import content.region.kandarin.quest.fishingcompo.FishingContest
import core.api.*
import core.game.activity.ActivityManager
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestRepository
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BonzoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        stage =
            if (args.size < 2) {
                if (inInventory(player, FishingContest.FISHING_ROD.id)) {
                    npc("Roll up, roll up! Enter the great Hemenster", "Fishing Contest! Only 5gp entrance fee!")
                    0
                } else {
                    npc("Sorry, lad, but you need a fishing", "rod to compete.")
                    100
                }
            } else {
                npc("Ok folks, time's up! Let's see who caught", "the biggest fish!")
                1000
            }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                options("I'll enter the competition please.", "No thanks, I'll just watch the fun.")
                stage++
            }

            1 ->
                when (buttonId) {
                    1 -> {
                        player("I'll enter the competition please.")
                        stage =
                            if (player.getAttribute("fishing_competition:garlic-stuffed", false)) {
                                50
                            } else {
                                10
                            }
                    }

                    2 -> {
                        player("No thanks, I'll just watch the fun.")
                        stage = 100
                    }
                }

            10 -> {
                npc("Marvelous!")
                stage++
            }

            11 ->
                if (!removeItem(player, Item(Items.COINS_995, 5), Container.INVENTORY)) {
                    player("I don't have the 5gp though...").also { stage++ }
                } else {
                    sendDialogue(player, "You pay Bonzo 5 coins").also { stage = 20 }
                }
            12 -> {
                npc("No pay, no play.")
                stage = 100
            }

            20 -> {
                npc(
                    "Ok, we've got all the fishermen! It's time",
                    "to roll! Ok, nearly everyone is in their",
                    "place already. You fish in the spot by the",
                )
                stage++
            }

            21 -> {
                npc("willow tree, and the Sinister Stranger, you fish by the pipes.")
                stage++
            }

            22 -> {
                player.dialogueInterpreter.sendDialogue("Your fishing competition spot is by the willow tree.")
                stage++
            }

            23 -> {
                ActivityManager.start(player, "Fishing Contest Cutscene", false)
                end()
            }

            100 -> end()
            1000 ->
                if (!inInventory(player, FishingContest.RAW_GIANT_CARP.id)) {
                    npc("And our winner is... the stranger who", "was fishing over by the pipes!").also { stage = 100 }
                } else {
                    player("I have a fish.").also { stage++ }
                }

            1001 -> {
                sendDialogue(player, "You hand over your fish.").also { stage++ }
            }

            1002 -> {
                npc(
                    "We have a new winner! The",
                    "heroic-looking person who was fishing",
                    "by the pipes has caught the biggest carp I've",
                    "seen since Grandpa Jack used to compete!",
                )
                stage++
            }

            1003 -> {
                if (removeItem(player, FishingContest.RAW_GIANT_CARP)) {
                    sendItemDialogue(
                        player,
                        FishingContest.FISHING_TROPHY.id,
                        "You are given the Hemenester fishing trophy!",
                    )
                    player.inventory.add(FishingContest.FISHING_TROPHY)
                    player.getQuestRepository().setStage(QuestRepository.getQuests()["Fishing Contest"], 20)
                }
                stage = 100
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BONZO_225)
    }
}
