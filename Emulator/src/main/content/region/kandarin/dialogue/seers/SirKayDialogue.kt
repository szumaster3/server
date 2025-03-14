package content.region.kandarin.dialogue.seers

import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class SirKayDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var STAGE_NOT_STARTED_MERLIN = 4
    var STAGE_UNFORTUNATELY_NOT = 5
    var STAGE_GET_MERLIN_OUT = 10
    var STAGE_MORDRED = 6
    var diaryLevel = 2

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!isQuestComplete(player, Quests.MERLINS_CRYSTAL)) {
            when (stage) {
                0 -> {
                    options("Hello.", "Talk about achievement diary.")
                    stage = 1
                }

                1 -> {
                    if (buttonId == 1) {
                        playerl(FaceAnim.NEUTRAL, "Hello.")
                        stage++
                    } else {
                        if (Diary.canReplaceReward(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)) {
                            player("I seem to have lost my seers' headband...")
                            stage = 35
                        } else if (Diary.hasClaimedLevelRewards(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)) {
                            player("Can you remind me what my headband does?")
                            stage = 40
                        } else if (Diary.canClaimLevelRewards(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)) {
                            player(
                                "Greetings, Sir Kay. I have completed all of the Hard",
                                "tasks in my Achievement Diary. May I have a reward?",
                            )
                            stage = 45
                        } else {
                            playerl(FaceAnim.NEUTRAL, "Hi! Can you help me out with the Achievement Diary tasks?")
                            stage = 60
                        }
                    }
                }

                2 -> {
                    npcl(FaceAnim.NEUTRAL, "Good morrow " + (if (player!!.isMale) "sirrah" else "madam") + "!")
                    stage++
                }

                3 -> {
                    if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 0) {
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Morning. Know where an adventurer has to go to find a quest around here?",
                        )
                        stage = STAGE_NOT_STARTED_MERLIN
                    } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 10) {
                        playerl(FaceAnim.NEUTRAL, "Any ideas on getting Merlin out of that crystal?")
                        stage = STAGE_GET_MERLIN_OUT
                    } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 20 ||
                        getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 30
                    ) {
                        playerl(FaceAnim.NEUTRAL, "Any ideas on getting into Mordred's fort?")
                        stage = STAGE_MORDRED
                    } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) >= 40) {
                        playerl(FaceAnim.NEUTRAL, "Any ideas on finding Excalibur?")
                        stage = STAGE_UNFORTUNATELY_NOT
                    }
                }

                STAGE_NOT_STARTED_MERLIN -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "An adventurer eh? There is no service finer than serving the bountiful King Arthur, and I happen to know there's an important quest to fulfill.",
                    )
                    stage = END_DIALOGUE
                }

                STAGE_UNFORTUNATELY_NOT -> {
                    npcl(FaceAnim.NEUTRAL, "Unfortunately not, " + (if (player!!.isMale) "sirrah" else "madam") + ".")
                    stage = END_DIALOGUE
                }

                6 -> {
                    npcl(FaceAnim.NEUTRAL, "Mordred... So you think he may be involved with the curse upon Merlin?")
                    stage++
                }

                7 -> {
                    playerl(FaceAnim.NEUTRAL, "Good a guess as any right?")
                    stage++
                }

                8 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I think you may be on to something there. Unfortunately his fortress is impregnable!",
                    )
                    stage++
                }

                9 -> {
                    playerl(FaceAnim.NEUTRAL, "... I'll figure something out.")
                    stage = END_DIALOGUE
                }

                STAGE_GET_MERLIN_OUT -> {
                    playerl(FaceAnim.NEUTRAL, "Any ideas on getting Merlin out of that crystal?")
                    stage = STAGE_UNFORTUNATELY_NOT
                }

                35 -> {
                    npcl(FaceAnim.NEUTRAL, "Here's your replacement. Please be more careful.")
                    Diary.grantReplacement(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                    stage = END_DIALOGUE
                }

                40 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Your headband will help you get experience when woodcutting maple trees, and an extra log or two when cutting normal trees. I've also told Geoff to increase",
                    )
                    stage++
                }

                41 -> {
                    npcl(FaceAnim.NEUTRAL, "your flax allowance in acknowledgement of your standing.")
                    stage = END_DIALOGUE
                }

                45 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Well done, young " + (if (player!!.isMale) "sir" else "madam") +
                            ". You must be a mighty adventurer indeed to have completed the Hard tasks.",
                    )
                    stage++
                }

                46 -> {
                    if (!removeItem(player!!, Items.SEERS_HEADBAND_1_14631)) {
                        npcl(FaceAnim.NEUTRAL, "I need your headband. Come back when you have it.")
                        stage = END_DIALOGUE
                    } else {
                        Diary.flagRewarded(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                        sendItemDialogue(
                            player!!,
                            Items.SEERS_HEADBAND_1_14631,
                            "You hand Sir Kay your headband and he concentrates for a moment. Some mysterious knightly energy passes through his hands and he gives the headband back to you, along with an old lamp.",
                        )
                        stage++
                    }
                }

                47 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "You will find that your headband now blesses you with the power to spin fabrics at extreme speed in Seers' Village. I will also instruct Geoff-erm-Flax to offer you a far larger flax allowance. Use your new powers",
                    )
                    stage++
                }

                48 -> {
                    npcl(FaceAnim.NEUTRAL, "wisely.")
                    stage++
                }

                49 -> {
                    playerl(FaceAnim.NEUTRAL, "Thank you, Sir Kay, I'll try not to harm anyone with my spinning.")
                    stage++
                }

                50 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "You are most welcome. You may also find that the Lady of the Lake is prepared to reward you for your services if you wear the headband in her presence.",
                    )
                    stage = END_DIALOGUE
                }

                60 -> {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "I'm afraid not. It is important that adventurers complete the tasks unaided. That way, only the truly worthy collect the spoils.",
                    )
                    stage = END_DIALOGUE
                }
            }
        } else {
            when (stage) {
                0 ->
                    npcl(FaceAnim.NEUTRAL, "Good morrow sirrah!").also {
                        if (getQuestStage(player!!, Quests.HOLY_GRAIL) == 0 ||
                            isQuestComplete(player!!, Quests.HOLY_GRAIL)
                        ) {
                            stage = 1
                        } else if (getQuestStage(player!!, Quests.HOLY_GRAIL) >= 10) {
                            stage = 10
                        }
                    }

                1 ->
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Sir Knight! Many thanks for your assistance in restoring Merlin to his former freedom!",
                    ).also {
                        stage++
                    }
                2 -> playerl(FaceAnim.NEUTRAL, "Hey, no problem.").also { stage = END_DIALOGUE }
                10 -> npcl(FaceAnim.NEUTRAL, "I hear you are questing for the Holy Grail?!").also { stage++ }
                11 -> playerl(FaceAnim.NEUTRAL, "That's right. Any hints?").also { stage++ }
                12 -> npcl(FaceAnim.NEUTRAL, "Unfortunately not, Sirrah.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SirKayDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_KAY_241)
    }
}
