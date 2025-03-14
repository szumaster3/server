package content.region.asgarnia.dialogue

import content.region.kandarin.quest.biohazard.dialogue.ChemistDialogue
import core.api.*
import core.api.quest.isQuestComplete
import core.api.quest.isQuestInProgress
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ChemistDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        setTitle(player, 4)
        if (!isQuestComplete(player, Quests.BIOHAZARD) && isQuestInProgress(player, Quests.BIOHAZARD, 10, 99)) {
            sendDialogueOptions(
                player,
                "Do you want to talk about lamps?",
                "Yes.",
                "Your quest.",
                "Impling jars.",
                "Falador Achievement Diary.",
            ).also {
                stage =
                    1
            }
        } else {
            sendDialogueOptions(
                player,
                "Do you want to talk about lamps?",
                "Yes.",
                "No.",
                "Impling jars.",
                "Falador Achievement Diary.",
            ).also {
                stage =
                    0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player("Hi, I need fuel for a lamp.").also { stage = 10 }
                    2 -> player("Hello.").also { stage = 20 }
                    3 -> player("I have a slightly odd question.").also { stage = 30 }
                    4 -> {
                        end()
                        openDialogue(player, ChemistDiaryDialogue())
                    }
                }
            1 ->
                when (buttonId) {
                    1 -> player("Hi, I need fuel for a lamp.").also { stage = 10 }
                    2 -> {
                        end()
                        openDialogue(player, ChemistDialogue())
                    }
                    3 -> player("I have a slightly odd question.").also { stage = 30 }
                    4 -> {
                        end()
                        openDialogue(player, ChemistDiaryDialogue())
                    }
                }
            10 -> npc("Hello there, the fuel you need is lamp oil, do you need", "help making it?").also { stage++ }
            11 -> player("Yes, please.").also { stage++ }
            12 ->
                npc(
                    "It's really quite simple. You use the small still in here.",
                    "It's all set up, so there's no fiddling around with dials...",
                ).also {
                    stage++
                }
            13 ->
                npc(
                    "Just put ordinary swamp tar in, and then use a lantern",
                    "or lamp to get the oil out.",
                ).also { stage++ }
            14 -> player("Thanks.").also { stage = END_DIALOGUE }
            20 -> npc("Oh.. hello, how's it going?").also { stage++ }
            21 -> player("Good thanks.").also { stage++ }
            22 -> npc("Good to hear, sorry but I have a few things to do", "now.").also { stage++ }
            23 -> player("Well I'd better let you get on then.").also { stage = END_DIALOGUE }
            30 -> npc("Jolly good, the odder the better. I like oddities.").also { stage++ }
            31 ->
                player(
                    "Do you know how I might distill a mix of anchovy oil",
                    "and flowers so that it forms a layer on the inside of a",
                    "butterfly jar?",
                ).also {
                    stage++
                }
            32 ->
                npc(
                    "That is an odd question. I commend you for it. Why",
                    "would you want to do that?",
                ).also { stage++ }
            33 ->
                player(
                    "Apparently, if I can make a jar like this it will be useful",
                    "for storing implings in.",
                ).also { stage++ }
            34 ->
                npc(
                    "My lamp oil still may be able to do what you want. Use the",
                    "oil and flower mix on the still.",
                ).also {
                    stage++
                }
            35 ->
                npc(
                    "Once that's done. Use one of those butterfly jars to",
                    "collect the distillate.",
                ).also { stage++ }
            36 -> player("Thanks!").also { stage++ }
            37 ->
                options(
                    "So how do you make anchovy oil?",
                    "Do you have a sieve I can use?",
                    "I'd better go and get the repellent.",
                ).also {
                    stage++
                }
            38 ->
                when (buttonId) {
                    1 -> player("So, how do you make anchovy oil?").also { stage = 50 }
                    2 -> player("Do you have a sieve I can use?").also { stage = 55 }
                    3 -> player("I'd better go and get the repellent.").also { stage = END_DIALOGUE }
                }
            50 ->
                npc(
                    "Anchovies are pretty oily fish. I'd have thought you",
                    "could just grind them up and sieve out the bits. You'd",
                    "probably want to remove any water first - Cooking",
                    "should do that pretty well.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            55 -> {
                if (player.hasItem(Item(Items.SIEVE_6097))) {
                    npc(
                        "Errm, yes. But you already have one. Two sieves is a ",
                        "bit exessive, don't you think?",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
                npc(
                    "Errm, yes. Here have this one. It's only been used for",
                    "sieving dead rats out of sewer water.",
                ).also { stage++ }
            }

            56 -> player("Err, why? Actually, on second thoughts I don't want to", "know.").also { stage++ }
            57 -> npc("Well, it would be ideally suited to your task.").also { stage++ }
            58 -> player("Fair enough.").also { stage++ }
            59 -> {
                end()
                addItemOrDrop(player, Items.SIEVE_6097)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return ChemistDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHEMIST_367)
    }
}
