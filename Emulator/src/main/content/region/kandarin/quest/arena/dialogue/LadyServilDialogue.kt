package content.region.kandarin.quest.arena.dialogue

import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LadyServilDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hi there, looks like you're in some trouble.")
        if (getQuestStage(player, Quests.FIGHT_ARENA) == 10) {
            playerl(FaceAnim.FRIENDLY, "Hello Lady Servil.")
        } else if (getQuestStage(player, Quests.FIGHT_ARENA) == 30) {
            playerl(FaceAnim.FRIENDLY, "Lady Servil, I have managed to infiltrate General Khazard's arena.")
        } else if (getQuestStage(player, Quests.FIGHT_ARENA) == 70) {
            playerl(
                FaceAnim.FRIENDLY,
                "Lady Servil. I freed your son, however he has returned to the arena to help your husband.",
            ).also {
                stage++
            }
        } else {
            playerl(FaceAnim.FRIENDLY, "Hello Lady Servil.")
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.FIGHT_ARENA)) {
            0 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.SAD,
                            "Oh I wish this broken cart was my only problem. *sob* I've got to find my family.. **sob**",
                        ).also {
                            stage++
                        }
                    1 -> options("I hope you can, good luck.", "Can I help you?").also { stage++ }
                    2 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.NEUTRAL, "I hope you can, good luck.").also { stage = END_DIALOGUE }
                            2 -> playerl(FaceAnim.FRIENDLY, "Can I help you?").also { stage++ }
                        }
                    3 -> npcl(FaceAnim.SAD, "Would you? Please?").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.SAD,
                            "I'm Lady Servil, and my husband is Sir Servil. We were travelling north together with our son Jeremy when we were ambushed by General Khazard's men.",
                        ).also {
                            stage++
                        }
                    5 -> playerl(FaceAnim.HALF_ASKING, "General Khazard?").also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.SAD,
                            "He's been after me even since I declined his hand in marriage.",
                        ).also { stage++ }
                    7 ->
                        npcl(
                            FaceAnim.SAD,
                            "Now he's kidnapped my husband and son to fight in his battle arena to the south of here. I hate to think what he'll do to them. He's a sick and twisted man.",
                        ).also {
                            stage++
                        }
                    8 -> playerl(FaceAnim.FRIENDLY, "I'll try my best to return your family.").also { stage++ }
                    9 -> {
                        end()
                        setQuestStage(player!!, Quests.FIGHT_ARENA, 10)
                        npcl(
                            FaceAnim.SAD,
                            "Please do. My family is wealthy and can reward you handsomely. I'll be waiting here for you.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    }
                }

            in 1..10 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.SAD, "Brave traveller, please... bring back my family.").also {
                            stage = END_DIALOGUE
                        }
                }

            in 11..20 ->
                when (stage) {
                    0 -> npcl(FaceAnim.WORRIED, "Have you had any luck with freeing my family?").also { stage++ }
                    1 ->
                        playerl(
                            FaceAnim.HAPPY,
                            "I've managed to get a guard's uniform, hopefully I can infiltrate the arena.",
                        ).also {
                            stage++
                        }
                    2 -> npcl(FaceAnim.SAD, "Please hurry.").also { stage = END_DIALOGUE }
                }

            in 21..30 ->
                when (stage) {
                    0 -> npcl(FaceAnim.ASKING, "And my family?").also { stage++ }
                    1 -> playerl(FaceAnim.NEUTRAL, "I'm working on it.").also { stage++ }
                    2 -> npcl(FaceAnim.SAD, "Please hurry.").also { stage = END_DIALOGUE }
                }

            in 31..98 ->
                when (stage) {
                    0 ->
                        npcl(FaceAnim.WORRIED, "Oh no, they won't stand a chance. Please go back and help.").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            99 ->
                when (stage) {
                    0 -> npcl(FaceAnim.AMAZED, "You're alive, I thought Khazard's men had taken you.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.HAPPY,
                            "My son and husband are safe and recovering at home. Without you they would certainly be dead. I am truly grateful for your service.",
                        ).also {
                            stage++
                        }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "All I can offer in return is material wealth. Please take these coins as a sign of my gratitude.",
                        ).also {
                            stage++
                        }
                    3 -> {
                        end()
                        finishQuest(player, Quests.FIGHT_ARENA)
                    }
                }
            100 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Oh hello my dear. My husband and son are resting while I wait for the cart fixer.",
                        ).also {
                            stage++
                        }
                    1 -> playerl(FaceAnim.FRIENDLY, "I hope he's not too long.").also { stage++ }
                    2 -> npcl(FaceAnim.FRIENDLY, "Thanks again for everything.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LADY_SERVIL_264)
    }
}
