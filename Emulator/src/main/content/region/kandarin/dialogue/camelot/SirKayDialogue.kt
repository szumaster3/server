package content.region.kandarin.dialogue.camelot

import content.data.GameAttributes
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.isQuestComplete
import core.api.quest.startQuest
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents Sir Kay dialogue.
 *
 * **Relations:**
 * - [Merlin's Crystal][content.region.kandarin.quest.merlin.MerlinCrystal]
 * - [Holy Grail][content.region.kandarin.quest.grail.HolyGrail]
 * - [TODO Kings Ransom][content.region.kandarin.quest.kr.KingsRansom]
 * - [Seers Achievement Diaries][content.region.kandarin.handlers.SeersVillageAchievementDiary]
 */
@Initializable
class SirKayDialogue(
    player: Player? = null,
) : Dialogue(player) {

    private var diaryLevel = 2

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Hello.", "Talk about Achievement Diary.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        // Diary consts.
        val alternateTeleport = getAttribute(player, GameAttributes.ATTRIBUTE_CAMELOT_ALT_TELE, false)
        val hardDiaryComplete = Diary.hasCompletedLevel(player!!, DiaryType.VARROCK, 0)

        // Quests consts.
        val completeMerlin = isQuestComplete(player, Quests.MERLINS_CRYSTAL)
        val completeHolyGrail = isQuestComplete(player, Quests.HOLY_GRAIL)
        val startHolyGrail = startQuest(player, Quests.HOLY_GRAIL)

        when (stage) {
            0 -> when (buttonId) {
                // Quest dialogues.
                1 -> npcl(FaceAnim.NEUTRAL, "Good morrow " + (if (player!!.isMale) "sirrah" else "madam") + "!").also { stage++ }
                // Achievement Diaries topics.
                2 -> showTopics(
                    IfTopic(
                        "I seem to have lost my seers' headband...",
                        35,
                        Diary.canReplaceReward(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                    ),
                    IfTopic(
                        "Can you remind me what my headband does?",
                        40,
                        Diary.hasClaimedLevelRewards(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                    ),
                    IfTopic(
                        "Greetings, Sir Kay. I have completed all of the Hard tasks in my Achievement Diary. May I have a reward?",
                        45,
                        Diary.canClaimLevelRewards(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                    ),
                    IfTopic(
                        "I'd like to change my teleport point.",
                        100,
                        isDiaryComplete(player, DiaryType.SEERS_VILLAGE, 2)
                    ),
                    Topic("Hi! Can you help me out with the Achievement Diary tasks?", 60)
                )
            }

            2 -> {
                // Post-quest dialogue: Merlin's Crystal.
                if (completeMerlin && startHolyGrail) {
                    when (stage) {
                        0 -> npcl(FaceAnim.HAPPY, "I hear you are questing for the Holy Grail?!").also { stage++ }
                        1 -> playerl(FaceAnim.HALF_ASKING, "That's right. Any hints?").also { stage++ }
                        2 -> npcl(FaceAnim.NEUTRAL, "Unfortunately not, " + (if (player!!.isMale) "sirrah" else "madam") + ".").also { stage = END_DIALOGUE }
                    }
                    return true
                }
                // Post-quest dialogue: Holy Grail.
                if(completeHolyGrail) {
                    when (stage) {
                        0 -> npcl(FaceAnim.HAPPY, "Sir Knight! Many thanks for your assistance in restoring Merlin to his former freedom!").also { stage++ }
                        1 -> playerl(FaceAnim.NEUTRAL, "Hey, no problem.").also { stage = END_DIALOGUE }
                    }
                    return true
                }
                // Merlin Crystal: Investigating how to find Excalibur.
                else {
                    if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 0) {
                        playerl(FaceAnim.NEUTRAL, "Morning. Know where an adventurer has to go to find a quest around here?")
                        stage = 4
                    } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 10) {
                        playerl(FaceAnim.NEUTRAL, "Any ideas on getting Merlin out of that crystal?")
                        stage = 10
                    } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 20 ||
                        getQuestStage(player!!, Quests.MERLINS_CRYSTAL) == 30
                    ) {
                        playerl(FaceAnim.NEUTRAL, "Any ideas on getting into Mordred's fort?")
                        stage = 6
                    } else if (getQuestStage(player!!, Quests.MERLINS_CRYSTAL) >= 40) {
                        playerl(FaceAnim.NEUTRAL, "Any ideas on finding Excalibur?")
                        stage = 5
                    }
                }
            }

            4 -> {
                npcl(FaceAnim.NEUTRAL, "An adventurer eh? There is no service finer than serving the bountiful King Arthur, and I happen to know there's an important quest to fulfill.")
                stage = END_DIALOGUE
            }

            5 -> {
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
                npcl(FaceAnim.NEUTRAL, "I think you may be on to something there. Unfortunately his fortress is impregnable!")
                stage++
            }

            9 -> {
                playerl(FaceAnim.NEUTRAL, "... I'll figure something out.")
                stage = END_DIALOGUE
            }

            10 -> {
                playerl(FaceAnim.NEUTRAL, "Any ideas on getting Merlin out of that crystal?")
                stage = 5
            }

            35 -> {
                npcl(FaceAnim.NEUTRAL, "Here's your replacement. Please be more careful.")
                Diary.grantReplacement(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                stage = END_DIALOGUE
            }

            40 -> {
                npcl(FaceAnim.NEUTRAL, "Your headband will help you get experience when woodcutting maple trees, and an extra log or two when cutting normal trees. I've also told Geoff to increase")
                stage++
            }

            41 -> {
                npcl(FaceAnim.NEUTRAL, "your flax allowance in acknowledgement of your standing.")
                stage = END_DIALOGUE
            }

            45 -> {
                npcl(FaceAnim.NEUTRAL, "Well done, young " + (if (player!!.isMale) "sir" else "madam") + ". You must be a mighty adventurer indeed to have completed the Hard tasks.",)
                stage++
            }

            46 -> {
                val diaryReward = DiaryManager(player).headband == diaryLevel
                if (!removeItem(player!!, diaryReward)) {
                    npcl(FaceAnim.NEUTRAL, "I need your headband. Come back when you have it.")
                    stage = END_DIALOGUE
                } else {
                    Diary.flagRewarded(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                    sendItemDialogue(player!!, diaryReward, "You hand Sir Kay your headband and he concentrates for a moment. Some mysterious knightly energy passes through his hands and he gives the headband back to you, along with an old lamp.")
                    stage++
                }
            }

            47 -> {
                npcl(FaceAnim.NEUTRAL, "You will find that your headband now blesses you with the power to spin fabrics at extreme speed in Seers' Village. I will also instruct Geoff-erm-Flax to offer you a far larger flax allowance. Use your new powers")
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
                npcl(FaceAnim.NEUTRAL, "You are most welcome. You may also find that the Lady of the Lake is prepared to reward you for your services if you wear the headband in her presence.")
                stage = END_DIALOGUE
            }

            60 -> {
                npcl(FaceAnim.NEUTRAL, "I'm afraid not. It is important that adventurers complete the tasks unaided. That way, only the truly worthy collect the spoils.")
                stage = END_DIALOGUE
            }

            // Achievements diaries: Camelot Teleport toggle.
            100 ->
                showTopics(
                    IfTopic("I'd like to teleport to the Seers' Village.", 101, hardDiaryComplete && !alternateTeleport),
                    IfTopic("I'd like to teleport to the gates of Camelot.", 102, hardDiaryComplete && alternateTeleport),
                    Topic("Nevermind.", END_DIALOGUE),
                )
            101 -> {
                npcl(FaceAnim.FRIENDLY, "There you are, your Camelot teleport will now take you to the Seers' Village.")
                sendMessage(player, core.tools.RED + "Camelot Teleport will now teleport you to the Seers' Village.")
                setAttribute(player!!, GameAttributes.ATTRIBUTE_CAMELOT_ALT_TELE, true)
                stage = END_DIALOGUE
            }
            102 -> {
                npcl(FaceAnim.FRIENDLY, "There you are, your Camelot teleport will now take you to the gates of Camelot.")
                sendMessage(player, core.tools.RED + "Camelot Teleport will now teleport you to the gates of Camelot.")
                setAttribute(player!!, GameAttributes.ATTRIBUTE_CAMELOT_ALT_TELE, false)
                stage = END_DIALOGUE
            }
        }
        return true
    }
    override fun newInstance(player: Player?): Dialogue = SirKayDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_KAY_241)
}
