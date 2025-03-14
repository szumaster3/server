package content.region.misthalin.quest.priest.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.updateQuestTab
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FatherAereckDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.THE_RESTLESS_GHOST)
        if (questStage == 10) {
            npc("Have you got rid of the ghost yet?")
            stage = 520
            return true
        }
        if (questStage == 20) {
            player("I had a talk with Father Urhney. He has given me this", "funny amulet to talk to the ghost with.")
            stage = 530
            return true
        }
        if (questStage == 30) {
            player(
                "I've found out that the ghost's corpse has lost its skull.",
                "If I can find the skull, the ghost should leave.",
            )
            stage = 540
            return true
        }
        if (questStage == 40) {
            player("I've finally found the ghost's skull!")
            stage = 550
            return true
        }
        if (isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
            npc(
                FaceAnim.HAPPY,
                "Thank you for getting rid of that awful ghost for me!",
                "May Saradomin always smile upon you!",
            )
            stage = 0
            return true
        }
        npc("Welcome to the church of holy Saradomin, my", "friend! What can I do for you today?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (isQuestComplete(player, Quests.THE_RESTLESS_GHOST)) {
                    setTitle(player, 4)
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "Can you change my gravestone now?",
                        "Who's Saradomin?",
                        "Nice place you've got here.",
                        "I'm looking for a quest.",
                    )
                    stage = 1
                } else {
                    setTitle(player, 4)
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "Can you change my gravestone now?",
                        "Who's Saradomin?",
                        "Nice place you've got here.",
                        "I'm looking for a quest.",
                    )
                    stage = 500
                }
            }

            1 ->
                when (buttonId) {
                    1 ->
                        npc(
                            "Certainly. All proceeds are donated to the",
                            "Varrockian Guards' Widows & Orphans Fund.",
                        ).also {
                            stage =
                                10
                        }

                    2 -> npc("Surely you have heard of our god, Saradomin?").also { stage = 20 }
                    3 -> npc("It is, isn't it? It was built over two centuries ago.").also { stage = END_DIALOGUE }
                    4 -> player(" I'm looking for a new quest.").also { stage = 32 }
                }

            10 -> {
                end()
                openInterface(player, Components.GRAVESTONE_SHOP_652)
            }

            20 ->
                npc(
                    "He who created the forces of goodness and purity in this",
                    "world? I cannot believe your ignorance!",
                ).also {
                    stage++
                }

            21 ->
                npc(
                    "This is the god with more followers than any other ...at",
                    "least in this part of the world.",
                ).also {
                    stage++
                }

            22 ->
                npc(
                    "He who forged the world as we know it, along with his",
                    "brothers Guthix and Zamorak?",
                ).also { stage++ }

            23 -> options("Oh, THAT Saradomin.", "Oh, sorry, I'm not from this world.").also { stage++ }
            24 ->
                when (buttonId) {
                    1 -> npc("There is only one Saradomin.").also { stage++ }
                    2 -> npc("...").also { stage = 26 }
                }

            25 -> player("Yeah. I, uh, thought you said something else.").also { stage = END_DIALOGUE }
            26 -> npc("That's...strange.").also { stage++ }
            27 ->
                npc(
                    "I thought things not from this world were all, you know,",
                    "slime and tentacles.",
                ).also { stage++ }

            28 -> options("Not me.", "I am! Do you like my disguise?").also { stage++ }
            29 ->
                when (buttonId) {
                    1 -> npc("Well, I can see that. Still, there's something special about", "you.").also { stage = 31 }
                    2 ->
                        npc(
                            "Argh! Avaunt, foul creature from another dimension!",
                            "Avaunt! Begone in the name of Saradomin!",
                        ).also { stage++ }
                }

            30 -> player("Okay, okay, I was only joking!").also { stage = END_DIALOGUE }
            31 -> player("Thanks, I think.").also { stage = END_DIALOGUE }
            32 -> npc("Sorry, I only had the one quest.").also { stage = END_DIALOGUE }

            500 ->
                when (buttonId) {
                    1 ->
                        npc(
                            "Certainly. All proceeds are donated to the",
                            "Varrockian Guards' Widows & Orphans Fund.",
                        ).also { stage = 10 }

                    2 -> npc("Surely you have heard of our god, Saradomin?").also { stage = 20 }
                    3 -> npc("It is, isn't it? It was built over two centuries ago.").also { stage = END_DIALOGUE }
                    4 -> player("I'm looking for a quest.").also { stage = 505 }
                }

            505 -> npc("That's lucky, I need someone to do a quest for me.").also { stage++ }
            506 -> options("Ok, let me help then.", "Sorry, I don't have time right now.").also { stage++ }
            507 ->
                when (buttonId) {
                    1 -> player("Ok, let me help then.").also { stage = 509 }
                    2 -> player("Sorry, I don't have time right now.").also { stage++ }
                }

            508 ->
                npc("Oh well. If you do have some spare time on your", "hands, come back and talk to me.").also {
                    stage =
                        END_DIALOGUE
                }

            509 ->
                npc(
                    "Thank you. The problem is, there is a ghost in the",
                    "church graveyard. I would like you to get rid of it.",
                ).also {
                    player.getQuestRepository().getQuest(Quests.THE_RESTLESS_GHOST).start(player)
                    updateQuestTab(player)
                    stage++
                }

            510 -> npc("If you need any help, my friend Father Urhney is an", "expert on ghosts.").also { stage++ }
            511 ->
                npc(
                    "I believe he is currently living as a hermit in Lumbridge",
                    "swamp. He has a little shack in the south-west of the",
                    "swamps.",
                ).also {
                    stage++
                }

            512 ->
                npc(
                    "Exit the graveyard through the south gate to reach the",
                    "swamp. I'm sure if you told him that I sent you he'd",
                    "be willing to help.",
                ).also {
                    stage++
                }

            513 -> npc("My name is Father Aereck by the way. Pleased to", "meet you.").also { stage++ }
            514 -> player("Likewise.").also { stage++ }
            515 ->
                npc(
                    "Take care travelling through the swamps, I have heard",
                    "they can be quite dangerous.",
                ).also { stage++ }

            516 -> player("I will, thanks.").also { stage = END_DIALOGUE }

            520 ->
                if (!player.gameAttributes.attributes.containsKey("restless-ghost:urhney")) {
                    player("I can't find Father Urhney at the moment.").also { stage++ }
                } else {
                    end()
                }

            521 ->
                npc(
                    "Well, you can get to the swamp he lives in by going",
                    "south through the cemetery.",
                ).also { stage++ }

            522 ->
                npc(
                    "You'll have to go right into the western depths of the",
                    "swamp, near the coastline. That is where his house is.",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            530 ->
                npc(
                    "I always wondered what that amulet was... Well, I hope",
                    "it's useful. Tell me when you get rid of the ghost!",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            540 -> npc("That WOULD explain it.").also { stage++ }
            541 -> npc("Hmmmmm. Well, I haven't seen any skulls.").also { stage++ }
            542 -> player("Yes, I think a warlock has stolen it.").also { stage++ }
            543 -> npc("I hate warlocks.").also { stage++ }
            544 -> npc("Ah well, good luck!").also { stage = END_DIALOGUE }
            550 -> npc("Great! Put it in the ghost's coffin and see what", "happens!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FATHER_AERECK_456)
    }
}
