package content.region.misthalin.dialogue

import core.api.freeSlots
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

class CouncilWorkmanDiaryDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.COUNCIL_WORKMAN_1287)
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hello").also { stage = 1 }
            1 -> options("So you fixed the bridge?", "About the Achievement Diary...").also { stage = 2 }
            2 ->
                when (buttonID) {
                    1 -> player(FaceAnim.HALF_ASKING, "So you fixed the bridge?").also { stage = 3 }
                    2 ->
                        if (player!!.achievementDiaryManager.getDiary(DiaryType.FREMENNIK)!!.isComplete(0, true) &&
                            !Diary.hasClaimedLevelRewards(player!!, DiaryType.FREMENNIK, 0)
                        ) {
                            player(FaceAnim.FRIENDLY, "I have question about my Achievement Diary").also { stage = 11 }
                        } else if (Diary.canReplaceReward(player!!, DiaryType.FREMENNIK, 0)) {
                            player(FaceAnim.FRIENDLY, "I need a new pair of boots.").also { stage = 15 }
                        } else {
                            player(FaceAnim.FRIENDLY, "I have question about my Achievement Diary").also { stage = 5 }
                        }
                }
            3 ->
                npc(
                    FaceAnim.HAPPY,
                    "Aye, that I did. 'Twas real thirsty work too. If only",
                    "some kind stranger would buy us a bit of bear to sup,",
                    "eh? What with that inn at Seers' Village so close by'",
                    "and all, eh?",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Just make sure yer don't get me none of that non",
                    "alcoholic rubbish from that poison salesman guy",
                    "at the tavern!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 ->
                options(
                    "What is the Achievement Diary?",
                    "What are the rewards?",
                    "How do I claim the rewards?",
                    "See you later.",
                ).also { stage++ }
            6 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.ASKING, "What is the Achievement Diary?").also { stage = 7 }
                    2 -> playerl(FaceAnim.ASKING, "What are the rewards?").also { stage = 17 }
                    3 -> playerl(FaceAnim.ASKING, "How do I claim the rewards?").also { stage = 10 }
                    4 -> playerl(FaceAnim.ASKING, "See you later.").also { stage = END_DIALOGUE }
                }
            7 ->
                npc(
                    FaceAnim.FRIENDLY,
                    "Very well; the Achievements Diary is a collection of",
                    "Tasks you may wish to complete while adventuring",
                    "world of ${GameWorld.settings!!.name}. You can earn special rewards",
                    "for completing Tasks.",
                ).also {
                    stage++
                }
            8 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Some also give items that will help to complete",
                    "other Tasks, and many count as progress towards",
                    "the set for the area they're in.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Some of the Fremennik diary tasks are simple, some will",
                    "require certain skill levels, and some might require",
                    "quests to be started or completed.",
                ).also {
                    stage =
                        5
                }
            10 ->
                npc(
                    "To claim your Fremennik Diaries rewards",
                    "speak to Yrsa in Rellekka, Advisor Ghrim on Miscellania,",
                    "or myself.",
                ).also {
                    stage =
                        5
                }
            11 ->
                player(
                    FaceAnim.HAPPY,
                    "I've completed all of the easy tasks in my Fremennik",
                    "Achievement Diary.",
                ).also {
                    stage++
                }
            12 ->
                npc(
                    FaceAnim.HAPPY,
                    "Ah, that's a relief. You can wear Fremennik sea boots",
                    "now - I have a pair for you.",
                ).also {
                    stage++
                }
            13 ->
                if (freeSlots(player!!) < 1) {
                    end()
                    npc(
                        FaceAnim.FRIENDLY,
                        "Although, you'll have to come back when you've the space",
                        "to take them.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npc(
                        FaceAnim.NEUTRAL,
                        "They're strange, them boots. I reckon they'll impress",
                        "the Fremennik and their strange spirits. Look at them",
                        "carefully, see if you can't work out what they do.",
                    ).also {
                        Diary.flagRewarded(player!!, DiaryType.FREMENNIK, 0)
                        stage = 14
                    }
                }
            14 -> player(FaceAnim.HAPPY, "Thanks!").also { stage = 5 }
            15 ->
                npc(
                    FaceAnim.HAPPY,
                    "Ah, that's a relief. You can wear Fremennik sea boots",
                    "now - I have a pair for you.",
                ).also {
                    stage++
                }
            16 ->
                if (freeSlots(player!!) < 1) {
                    end()
                    npc(
                        FaceAnim.FRIENDLY,
                        "Although, you'll have to come back when you've the space",
                        "to take them.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npc(
                        FaceAnim.NEUTRAL,
                        "They're strange, them boots. I reckon they'll impress",
                        "the Fremennik and their strange spirits. Look at them",
                        "carefully, see if you can't work out what they do.",
                    ).also {
                        Diary.grantReplacement(player!!, DiaryType.FREMENNIK, 0)
                        stage = 14
                    }
                }
            17 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "For completing the Fremennik diaries, you are presented",
                    "with a pair of sea boots. These boots will become increasingly",
                    "useful with each difficulty level of the set that you complete.",
                ).also {
                    stage++
                }
            18 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "When you are presented with your rewards,",
                    "you will be told of their uses.",
                ).also {
                    stage =
                        5
                }
        }
    }
}
