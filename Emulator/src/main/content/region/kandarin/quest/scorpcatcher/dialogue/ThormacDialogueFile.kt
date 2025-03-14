package content.region.kandarin.quest.scorpcatcher.dialogue

import content.region.kandarin.quest.scorpcatcher.ScorpionCatcher
import content.region.kandarin.quest.scorpcatcher.handlers.ScorpionCatcherListeners.Companion.getCage
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.component.Component
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class ThormacDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val questName = Quests.SCORPION_CATCHER
        val questStage = getQuestStage(player!!, questName)
        val hasAnCage =
            hasAnItem(
                player!!,
                Items.SCORPION_CAGE_456,
                Items.SCORPION_CAGE_457,
                Items.SCORPION_CAGE_458,
                Items.SCORPION_CAGE_459,
                Items.SCORPION_CAGE_460,
                Items.SCORPION_CAGE_461,
                Items.SCORPION_CAGE_462,
                Items.SCORPION_CAGE_463,
            ).container !=
                null
        val wearHeadBand = inEquipment(player!!, Items.SEERS_HEADBAND_1_14631)
        npc = NPC(NPCs.THORMAC_389)
        when (questStage) {
            0 -> {
                when (stage) {
                    0 -> {
                        if (!hasLevelStat(player!!, Skills.PRAYER, 31)) {
                            sendMessage(player!!, "Thormac is not interested in talking.")
                        } else if (getAttribute(player!!, ScorpionCatcher.ATTRIBUTE_CAGE, false)) {
                            npcl(
                                FaceAnim.NEUTRAL,
                                "If you go up to the village of Seers, to the North of here, one of them will be able to tell you where the scorpions are now.",
                            ).also {
                                stage =
                                    21
                            }
                        } else {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Hello I am Thormac the sorcerer. I don't suppose you could be of assistance to me?",
                            ).also {
                                stage++
                            }
                        }
                    }

                    1 -> options("What do you need assistance with?", "I'm a little busy.").also { stage++ }
                    2 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "What do you need assistance with?").also { stage = 5 }
                            2 -> playerl(FaceAnim.NEUTRAL, "I'm a little busy.").also { stage = END_DIALOGUE }
                        }

                    5 ->
                        npcl(
                            FaceAnim.SAD,
                            "I've lost my pet scorpions. They're lesser Kharid scorpions, a very rare breed.",
                        ).also {
                            stage++
                        }
                    6 ->
                        npcl(
                            FaceAnim.SAD,
                            "I left their cage door open, now I don't know where they've gone.",
                        ).also { stage++ }
                    7 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "There's three of them, and they're quick little beasties. They're all over" +
                                GameWorld.settings!!.name +
                                ".",
                        ).also { stage++ }
                    8 ->
                        options(
                            "So how would I go about catching them then?",
                            "What's in it for me?",
                            "I'm not interested then.",
                        ).also {
                            stage++
                        }
                    9 ->
                        when (buttonID) {
                            1 ->
                                playerl(FaceAnim.HALF_ASKING, "So how would I go about catching them then?").also {
                                    stage =
                                        16
                                }
                            2 -> playerl(FaceAnim.HALF_ASKING, "What's in it for me?").also { stage = 13 }
                            3 -> playerl(FaceAnim.NEUTRAL, "I'm not interested then.").also { stage = 11 }
                        }

                    11 ->
                        npcl(FaceAnim.NEUTRAL, "Blast, I suppose I'll have to find someone else then.").also {
                            stage =
                                END_DIALOGUE
                        }
                    13 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well I suppose I can aid you with my skills as a staff sorcerer. Most battlestaves around here are a bit puny. I can beef them up for you a bit.",
                        ).also {
                            stage++
                        }
                    14 ->
                        options(
                            "So how would I go about catching them then?",
                            "I'm not interested then.",
                        ).also { stage++ }
                    15 ->
                        when (buttonID) {
                            1 ->
                                playerl(FaceAnim.HALF_ASKING, "So how would I go about catching them then?").also {
                                    stage =
                                        16
                                }
                            2 -> playerl(FaceAnim.NEUTRAL, "I'm not interested then.").also { stage = 11 }
                        }

                    16 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well I have a scorpion cage here which you can use to catch them in.",
                        ).also { stage++ }
                    17 ->
                        runTask(player!!, 0) {
                            lock(player!!, 6)
                            lockInteractions(npc!!, 6)
                            getCage(player!!)
                            player!!.interfaceManager.close(Component(Components.NPCCHAT1_241))
                        }

                    18 ->
                        options(
                            "Ok, I will do it then",
                            "What's in it for me?",
                            "I'm not interested then.",
                        ).also { stage++ }

                    19 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "Ok, I will do it then").also { stage = END_DIALOGUE }
                            2 -> playerl(FaceAnim.HALF_ASKING, "What's in it for me?").also { stage = 20 }
                            3 -> playerl(FaceAnim.NEUTRAL, "I'm not interested then.").also { stage = 11 }
                        }

                    20 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Well I suppose I can aid you with my skills as a staff sorcerer. Most battlestaves around here are a bit puny. I can beef them up for you a bit.",
                        ).also {
                            stage++
                        }

                    21 ->
                        options("Ok, I will do it then", "I'm not interested then.").also {
                            removeAttribute(player!!, ScorpionCatcher.ATTRIBUTE_CAGE)
                            stage++
                        }

                    22 ->
                        when (buttonID) {
                            1 ->
                                playerl(FaceAnim.NEUTRAL, "Ok, I will do it then.").also {
                                    setQuestStage(player!!, Quests.SCORPION_CATCHER, 10)
                                    stage = END_DIALOGUE
                                }

                            2 -> playerl(FaceAnim.NEUTRAL, "I'm not interested then.").also { stage = 11 }
                        }
                }
            }
            in 1..50 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.HALF_ASKING, "How goes your quest?").also { stage++ }
                    1 ->
                        if (!hasAnCage) {
                            playerl(FaceAnim.NEUTRAL, "I've lost my cage.").also { stage++ }
                        } else if (inInventory(player!!, Items.SCORPION_CAGE_463)) {
                            playerl(FaceAnim.NEUTRAL, "I have retrieved all your scorpions.").also { stage = 5 }
                        } else {
                            playerl(FaceAnim.NEUTRAL, "I've not caught all the scorpions yet.").also { stage = 4 }
                        }

                    2 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Ok, here's another cage. You're almost as bad at losing things as me.",
                        ).also { stage++ }
                    3 -> {
                        end()
                        player!!.questRepository.setStageNonmonotonic(player!!.questRepository.forIndex(108), 10)
                        removeAttribute(player!!, "scorpion_catcher:caught_taverly")
                        removeAttribute(player!!, "scorpion_catcher:caught_barb")
                        removeAttribute(player!!, "scorpion_catcher:caught_monk")
                        addItemOrDrop(player!!, Items.SCORPION_CAGE_456)
                        stage = END_DIALOGUE
                    }

                    4 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Well remember to go speak to the Seers, ",
                            "North of here, if you need any help.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 -> npcl(FaceAnim.NEUTRAL, "Aha, my little scorpions home at last!").also { stage++ }
                    6 -> {
                        end()
                        finishQuest(player!!, Quests.SCORPION_CATCHER)
                        removeItem(player!!, Items.SCORPION_CAGE_463)
                        sendMessage(player!!, "You don't need that scorpion cage anymore.")
                        stage = END_DIALOGUE
                    }
                }
            }
            100 -> {
                when (stage) {
                    0 -> npcl(FaceAnim.NEUTRAL, "Thank you for rescuing my scorpions.").also { stage++ }
                    1 -> options("That's okay.", "You said you'd enchant my battlestaff for me.").also { stage++ }
                    2 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.NEUTRAL, "That's okay.").also { stage = END_DIALOGUE }
                            2 ->
                                playerl(
                                    FaceAnim.NEUTRAL,
                                    "You said you'd enchant my battlestaff for me.",
                                ).also { stage++ }
                        }

                    3 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Yes, although it'll cost you " + (if (wearHeadBand) "27,000" else "40,000") +
                                " coins for the",
                            "materials. What kind of staff did you want enchanting?",
                        ).also { stage++ }
                    4 -> {
                        end()
                        openInterface(player!!, Components.STAFF_ENCHANT_332)
                    }
                }
            }
        }
    }
}
