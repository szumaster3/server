package content.region.kandarin.camelot.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Sir Kay dialogue.
 */
@Initializable
class SirKayDialogue(player: Player? = null) : Dialogue(player) {

    private var diaryLevel = 2

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Hello.", "Talk about Achievement Diary.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Diary consts.
        val alternateTeleport = getAttribute(player, GameAttributes.ATTRIBUTE_CAMELOT_ALT_TELE, false)
        val hardDiaryComplete = Diary.hasCompletedLevel(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)

        // Quest consts.
        var merlinQuestStage = getQuestStage(player!!, Quests.MERLINS_CRYSTAL)
        var holyGrailQuestStage = getQuestStage(player!!, Quests.HOLY_GRAIL)

        when (stage) {
            0 -> when (buttonId) {
                // Quest topics.
                1 -> npcl(FaceAnim.NEUTRAL, "Good morrow " + (if (player!!.isMale) "sirrah" else "madam") + "!").also { stage = 2 }
                // Achievement topics.
                2 -> showTopics(
                    IfTopic("I seem to have lost my seers' headband...", 10, Diary.canReplaceReward(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)),
                    IfTopic("Can you remind me what my headband does?", 11, Diary.hasClaimedLevelRewards(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)),
                    IfTopic("I have question about my Achievement Diary.", 16, Diary.canClaimLevelRewards(player, DiaryType.SEERS_VILLAGE, diaryLevel), true),
                    IfTopic("I'd like to change my teleport point.", 24, hardDiaryComplete),
                    Topic("Hi! Can you help me out with the Achievement Diary tasks?", 23)
                )
            }
            2 -> {
                if (holyGrailQuestStage == 10) {
                    npcl(FaceAnim.HAPPY, "I hear you are questing for the Holy Grail?!").also { stage = 4 }
                } else when (merlinQuestStage) {
                    100 -> npcl(FaceAnim.HAPPY, "${if (player!!.isMale) "sirrah" else "madam"}! Many thanks for your assistance in restoring Merlin to his former freedom!").also { stage = 3 }
                    10 -> playerl(FaceAnim.HALF_ASKING, "Any ideas on getting Merlin out of that crystal?").also { stage = 5 }
                    in 20..30 -> playerl(FaceAnim.HALF_ASKING, "Any ideas on getting into Mordred's fort?").also { stage = 6 }
                    40 -> playerl(FaceAnim.HALF_ASKING, "Any ideas on finding Excalibur?").also { stage = 5 }
                    else -> player(FaceAnim.HAPPY, "Morning. Know where an adventurer has to go to find a quest around here?").also { stage = 5 }
                }
            }
            3 -> playerl(FaceAnim.NEUTRAL, "Hey, no problem.").also { stage = END_DIALOGUE }
            4 -> playerl(FaceAnim.HALF_ASKING, "That's right. Any hints?").also { stage++ }
            5 -> npcl(FaceAnim.NEUTRAL, "Unfortunately not, " + (if (player!!.isMale) "sirrah" else "madam") + ".").also { stage = END_DIALOGUE }
            6 -> npcl(FaceAnim.NEUTRAL, "Mordred... So you think he may be involved with the curse upon Merlin?").also { stage++ }
            7 -> playerl(FaceAnim.NEUTRAL, "Good a guess as any right?").also { stage++ }
            8 -> npcl(FaceAnim.NEUTRAL, "I think you may be on to something there. Unfortunately his fortress is impregnable!").also { stage++ }
            9 -> playerl(FaceAnim.NEUTRAL, "...I'll figure something out.").also { stage = END_DIALOGUE }
            10 -> {
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.HALF_GUILTY, "Sorry friend, you'll need more inventory space to get the reward.").also { stage = END_DIALOGUE }
                    return true
                }
                npcl(FaceAnim.NEUTRAL, "Here's your replacement. Please be more careful.")
                Diary.grantReplacement(player!!, DiaryType.SEERS_VILLAGE, diaryLevel)
                stage = END_DIALOGUE
            }
            11 -> npcl(FaceAnim.NEUTRAL, "While wearing it, 'Flax' will give you 120 flax per day, he will make you faster at spinning, you'll be able to see better in the dark and both your Prayer and defence against magic attacks will be boosted.").also { stage++ }
            12 -> npcl(FaceAnim.NEUTRAL, "Stankers will allow 308 pieces of coal in his coal truck, and you'll get bonuses when cutting maple and normal trees. I'm not finished! Your Camelot Teleport spell can be switched to Seers' Village and Thormac will lower his prices somewhat.").also { stage++ }
            13 -> npcl(FaceAnim.HAPPY, "Phew! Oh, and how could I forget + you'll get a bonus when you use the altar in the chapel here, but you don't need to wear the headband for that.").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "Thank you, Sir Kay. I'll try not to harm anyone with my spinning.").also { stage++ }
            15 -> npcl(FaceAnim.HAPPY, "You are most welcome. You may also find that the Lady of the Lake is prepared to reward you for your services if you wear the headband in her presence.").also { stage = END_DIALOGUE }
            16 -> playerl(FaceAnim.HAPPY, "Greetings, Sir Kay. I have completed all of the Hard tasks in my Achievement Diary. May I have a reward?").also { stage++ }
            17 -> {
                if (freeSlots(player) == 0) {
                    npcl(FaceAnim.HALF_GUILTY, "Sorry friend, you'll need more inventory space to get the reward.").also { stage = END_DIALOGUE }
                    return true
                }
                if (!Diary.flagRewarded(player, DiaryType.SEERS_VILLAGE, diaryLevel)) return true
                sendItemDialogue(player, Diary.getRewards(DiaryType.SEERS_VILLAGE, diaryLevel)[0], "You hand Sir Kay your headband and he concentrates for a moment. Some mysterious knightly energy passes through his hands and he gives the headband back to you, along with an old lamp.").also { stage++ }
            }
            19 -> npcl(FaceAnim.NEUTRAL, "You will find that your headband now blesses you with the power to spin fabrics at extreme speed in Seers' Village. I will also instruct Geoff-erm-Flax to offer you a far larger flax allowance. Use your new powers").also { stage++ }
            20 -> npcl(FaceAnim.NEUTRAL, "wisely.").also { stage++ }
            21 -> playerl(FaceAnim.NEUTRAL, "Thank you, Sir Kay, I'll try not to harm anyone with my spinning.").also { stage++ }
            22 -> npcl(FaceAnim.NEUTRAL, "You are most welcome. You may also find that the Lady of the Lake is prepared to reward you for your services if you wear the headband in her presence.").also { stage = END_DIALOGUE }
            23 -> npcl(FaceAnim.NEUTRAL, "I'm afraid not. It is important that adventurers complete the tasks unaided. That way, only the truly worthy collect the spoils.").also { stage = END_DIALOGUE }
            // Achievements diaries: Camelot Teleport toggle.
            24 -> showTopics(
                IfTopic("I'd like to teleport to the Seers' Village.", 25, hardDiaryComplete && !alternateTeleport),
                IfTopic("I'd like to teleport to the gates of Camelot.", 26, hardDiaryComplete && alternateTeleport),
                Topic("Nevermind.", END_DIALOGUE),
            )
            25 -> {
                npcl(FaceAnim.FRIENDLY, "There you are, your Camelot teleport will now take you to the Seers' Village.")
                sendMessage(player, core.tools.RED + "Camelot Teleport will now teleport you to the Seers' Village.")
                setAttribute(player!!, GameAttributes.ATTRIBUTE_CAMELOT_ALT_TELE, true)
                stage = END_DIALOGUE
            }
            26 -> {
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
