package content.region.kandarin.quest.sheepherder.dialogue

import core.api.addItemOrDrop
import core.api.hasAnItem
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HalgriveDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        if (getQuestStage(player, Quests.SHEEP_HERDER) < 10) {
            player("Hello. How are you?")
            stage = 0
            return true
        } else {
            npc("Have you managed to find and dispose of those four", "plague-bearing sheep yet?")
            stage = 200
            return true
        }
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                npc("I've been better.")
                stage++
            }

            1 -> {
                options("What's wrong?", "That's life for you.")
                stage++
            }

            2 ->
                when (buttonId) {
                    1 -> {
                        player("What's wrong?")
                        stage = 10
                    }

                    2 -> {
                        player("That's life for you.")
                        stage = 108
                    }
                }

            10 -> {
                npc(
                    "You may or may not be aware, but a plague has",
                    "spread across Western Ardougne. Now, so far, our",
                    "efforts to contain it have been largely successful, for the",
                    "most part.",
                )
                stage++
            }

            11 -> {
                npc(
                    "However, four sheep recently escaped from a farm near",
                    "the city. When they were found, we noticed that they",
                    "were strangely discoloured, so we asked the mourners",
                    "to examine them.",
                )
                stage++
            }

            12 -> {
                npc("They found that the sheep had become infected with the", "plague.")
                stage++
            }

            13 -> {
                npc(
                    "As the councillor responsible for public health and safety",
                    "here in East Ardougne I am looking for someone to",
                    "herd these sheep into a safe enclosure, kill them quickly",
                    "and cleanly and then dispose of the remains hygienically",
                )
                stage++
            }

            14 -> {
                npc("in a special incinerator.")
                stage++
            }

            15 -> {
                npc(
                    "Unfortunately nobody wants to risk catching the plague,",
                    "and I am unable to find someone willing to undertake",
                    "this mission for me.",
                )
                stage++
            }

            16 -> {
                options("I can do that for you.", "That's not a job for me.")
                stage++
            }

            17 ->
                when (buttonId) {
                    1 -> {
                        player("I can do that for you.")
                        stage = 100
                    }

                    2 -> {
                        player("That's not a job for me.")
                        stage = 108
                    }
                }

            100 -> {
                npc(
                    "Y-you will??? That is excellent news! Head to the",
                    "enclosure we have set up on Farmer Brumty's land to",
                    "the north of the city; the four infected sheep should still",
                    "be somewhere in that vicinity. Before you will be allowed",
                )
                stage++
            }

            101 -> {
                npc(
                    "to enter the enclosure, however, you must ensure you",
                    "have some kind of protective clothing to prevent",
                    "contagion.",
                )
                stage++
            }

            102 -> {
                player("Where can I find some protective clothing then?")
                stage++
            }

            103 -> {
                npc(
                    "Doctor Orbon wears it when conducting mercy",
                    "missions to the infected parts of the city. You should be",
                    "able to find him in the chapel just north of here. Please",
                    "also take this poisoned sheep feed; we believe poisoning",
                )
                stage++
            }

            104 -> {
                npc(
                    "the sheep will minimise the risk of airborne",
                    "contamination, and is of course also more humane to",
                    "the sheep.",
                )
                stage++
            }

            105 -> {
                player.getQuestRepository().getQuest(Quests.SHEEP_HERDER).start(player)
                player.dialogueInterpreter.sendDialogue("The councillor gives you some poisoned sheep feed.")
                player.inventory.add(content.region.kandarin.quest.sheepherder.SheepHerder.POISON)
                stage++
            }

            106 -> {
                player("How will I know which sheep are infected?")
                stage++
            }

            107 -> {
                npc(
                    "The poor creatures have developed strangely discoloured",
                    "wool and flesh. You should have no trouble spotting",
                    "them.",
                )
                stage++
            }

            108 -> end()
            200 -> {
                if (player.getAttribute("sheep_herder:all_dead", false)) {
                    player("Yes, I have.")
                    stage = 205
                } else {
                    player("No, I haven't.")
                    stage++
                }
            }

            201 -> {
                npc("Well, please do hurry!")
                stage++
            }

            202 -> {
                if (hasAnItem(player, Items.SHEEP_FEED_279).container != null) {
                    player("I'll do my best sir.")
                    stage = 108
                } else {
                    player("Some more sheep poison would be appreciated...")
                    stage++
                }
            }

            203 -> {
                addItemOrDrop(player, Items.SHEEP_FEED_279, 1)
                npc("Certainly adventurer. Please hurry!")
                stage = 108
            }

            205 -> {
                npc(
                    "Excellent work adventurer! Please let me reimburse",
                    "you the 100 gold it cost you to purchase your",
                    "protective clothing.",
                )
                stage++
            }

            206 -> {
                npc(
                    "And in recognition of your service to the public health",
                    "of Ardougne please accept this further 3000 coins as a",
                    "reward.",
                )
                stage++
            }

            207 -> {
                finishQuest(player!!, "Sheep Herder")
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return HalgriveDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COUNCILLOR_HALGRIVE_289)
    }
}
