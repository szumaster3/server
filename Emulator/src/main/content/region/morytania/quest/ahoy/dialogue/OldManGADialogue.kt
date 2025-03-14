package content.region.morytania.quest.ahoy.dialogue

import content.region.morytania.quest.ahoy.handlers.GhostsAhoyUtils
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class OldManGADialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questStage = getQuestStage(player!!, Quests.GHOSTS_AHOY)
        npc = NPC(NPCs.OLD_MAN_1696)
        val hasKey = hasAnItem(player!!, Items.CHEST_KEY_2404).container != null
        when (questStage) {
            in 4..5 ->
                when (stage) {
                    0 ->
                        if (!hasKey && questStage >= 5) {
                            options(
                                "What is your name?",
                                "Can I have the key to the chest?",
                                "Is this your toy boat?",
                            ).also { stage++ }
                        } else {
                            options(
                                "What is your name?",
                                "I've lost my key to the chest - do you have another one?",
                                "Is this your toy boat?",
                            ).also {
                                stage++
                            }
                        }

                    1 ->
                        when (buttonID) {
                            1 -> player("What is your name?").also { stage++ }
                            2 -> {
                                if (!hasKey && questStage >= 5) {
                                    player(
                                        "I've lost my key to the chest - do you have another one?",
                                    ).also { stage = 19 }
                                } else {
                                    player("Can I have the key to the chest?").also { stage = 5 }
                                }
                            }

                            3 -> player("Is this your toy boat?").also { stage = 8 }
                        }

                    2 -> npc("I don't remember. Everyone around here just", "calls me 'boy'.").also { stage++ }
                    3 -> player("You're the cabin boy?!?").also { stage++ }
                    4 -> npc("Yes, and proud of it.").also { stage = END_DIALOGUE }
                    5 -> npc("Hang on, let me ask the Captain ...").also { stage++ }
                    6 ->
                        sendDialogue(
                            player!!,
                            "The old man cocks an ear towards the Pirate Captain's skeleton.",
                        ).also { stage++ }
                    7 -> npc("The Captain says no.").also { stage = END_DIALOGUE }
                    8 -> sendDialogue(player!!, "The old man inspects the toy boat.").also { stage++ }
                    9 -> {
                        if (inInventory(player!!, Items.MODEL_SHIP_4253)) {
                            npc("No - I made a toy boat a long while ago, but", "that one had a flag.").also {
                                stage = END_DIALOGUE
                            }
                        } else if (inInventory(player!!, Items.MODEL_SHIP_4254) &&
                            getAttribute(player!!, GhostsAhoyUtils.rightShip, false)
                        ) {
                            npc(
                                "My word - so it is!!! I never thought I would see it again!!",
                                "Where did you get it from?",
                            ).also { stage++ }
                        } else if (inInventory(player!!, Items.MODEL_SHIP_4254)) {
                            npc(
                                "No - I made a toy boat a long while ago, but the colours",
                                "on the flag were different.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            npc("Come back if we have anything to", "talk about.").also { stage = END_DIALOGUE }
                        }
                    }
                    10 -> player("Your mother gave it to me to pass on to you.").also { stage++ }
                    11 -> npc("My mother? She still lives?").also { stage++ }
                    12 -> player("Yes, in a shack to the west of here.").also { stage++ }
                    13 -> npc("After all these years ...").also { stage++ }
                    14 -> player("Can I have the key to the chest, then?").also { stage++ }
                    15 -> npc("Hang on, let me ask the Captain ...").also { stage++ }
                    16 ->
                        sendDialogue(
                            player!!,
                            "The old man cocks an ear towards the skeleton of the Pirate Captain.",
                        ).also { stage++ }
                    17 ->
                        npc("The Captain says yes.").also {
                            removeItem(player!!, Items.MODEL_SHIP_4254)
                            stage++
                        }

                    18 -> {
                        end()
                        sendItemDialogue(
                            player!!,
                            Items.CHEST_KEY_2404,
                            "The old man gives you the chest key.",
                        ).also { stage++ }
                        addItemOrDrop(player!!, Items.CHEST_KEY_2404)
                        setQuestStage(player!!, Quests.GHOSTS_AHOY, 5)
                    }
                    19 ->
                        npc(
                            "What? But the chest is only just over there!",
                            "How on Gielinor did you lose it in that short distance?",
                        ).also {
                            stage++
                        }
                    20 -> player("Sorry, I won't do it again...").also { stage++ }
                    21 -> {
                        end()
                        sendItemDialogue(
                            player!!,
                            Items.CHEST_KEY_2404,
                            "The old man grudgingly gives you another chest key.",
                        ).also { stage++ }
                        addItemOrDrop(player!!, Items.CHEST_KEY_2404)
                    }
                }

            6 ->
                when (stage) {
                    0 -> player("How is it going?").also { stage++ }
                    1 -> npc("Wonderful, wonderful! Mother's coming to get me!").also { stage = END_DIALOGUE }
                }
        }
    }
}
