package content.region.asgarnia.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import org.rs.consts.NPCs

class ChemistDiaryDialogue : DialogueFile() {
    private val level = 1

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CHEMIST_367)
        when (stage) {
            0 -> {
                if (Diary.canReplaceReward(player!!, DiaryType.FALADOR, level)) {
                    player("I seem to have lost my Falador shield...").also { stage = 80 }
                } else {
                    npc("How are you getting on with the Achievement Diary?").also { stage = 90 }
                }
            }
            80 -> {
                Diary.grantReplacement(player!!, DiaryType.FALADOR, level)
                npc("Here's your replacement. Please be more careful.").also { stage = 999 }
            }
            90 -> options("I've come for my reward.", "I'm doing good.", "I have a question.").also { stage = 91 }
            91 ->
                when (buttonID) {
                    1 -> player("I've come for my reward.").also { stage = 200 }
                    2 -> player("I'm doing good.").also { stage = 220 }
                    3 -> player("I have a question.").also { stage = 105 }
                }
            105 -> {
                if (!Diary.hasClaimedLevelRewards(player!!, DiaryType.FALADOR, level)) {
                    options(
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later.",
                    ).also {
                        stage =
                            106
                    }
                } else {
                    options(
                        "Can you remind me what my Falador shield does, please?",
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later.",
                    ).also {
                        stage =
                            107
                    }
                }
            }
            106 ->
                when (buttonID) {
                    1 -> player("What is the Achievement Diary?").also { stage = 110 }
                    2 -> player("What are the rewards?").also { stage = 120 }
                    3 -> player("How do I claim the rewards?").also { stage = 130 }
                    4 -> player("See you later.").also { stage = 999 }
                }
            107 ->
                when (buttonID) {
                    1 -> player("Can you remind me what my Falador shield does, please?").also { stage = 150 }
                    2 -> player("What is the Achievement Diary?").also { stage = 110 }
                    3 -> player("What are the rewards?").also { stage = 120 }
                    4 -> player("How do I claim the rewards?").also { stage = 130 }
                    5 -> player("See you later.").also { stage = 999 }
                }
            110 ->
                npc(
                    "Ah, well it's a diary that helps you keep track of",
                    "particular achievements you've made here on",
                    "Gielinor.",
                ).also {
                    stage++
                }
            111 ->
                npc(
                    "If you manage to complete a particular set of tasks,",
                    "you will be rewarded for your explorative efforts.",
                ).also {
                    stage++
                }
            112 ->
                npc(
                    "You can access your Achievement Diary by going to",
                    "the Quest Journal, then clicking on the green star icon",
                    "in the top-right hand corner.",
                ).also {
                    stage =
                        105
                }
            120 ->
                npc(
                    "Ah, well there are different rewards for each",
                    "Achievement Diary. For completing each stage of the",
                    "Falador diary, you are presented with a Falador shield.",
                ).also {
                    stage++
                }
            121 -> npc("There are three shields available, one for each difficulty", "level.").also { stage++ }
            122 ->
                npc("When you are presented with your rewards, you will", "be told of their uses.").also {
                    stage =
                        105
                }
            130 ->
                npc(
                    "You need to complete all of the tasks in a particular",
                    "difficulty, then you can claim your reward.",
                ).also {
                    stage++
                }
            131 ->
                npc(
                    "Some of Falador's tasks are simple, some will require",
                    "certain skill levels, and some might require quests to be",
                    "started or completed.",
                ).also {
                    stage++
                }
            132 ->
                npc(
                    "To claim your Falador Achievement Diary rewards,",
                    "speak to Redbeard the Pirate in Port Sarim, Sir Vyvin's",
                    "squire in the White Knight's Castle, or myself.",
                ).also {
                    stage =
                        105
                }
            150 ->
                npc(
                    "This is the second stage of the Falador shield: a kite",
                    "shield. It grants you all the benefits fo the buckler, but",
                    "with increased Prayer restore, and Farming experience",
                    "when using the patches near Falador.",
                ).also {
                    stage++
                }
            151 ->
                npc(
                    "As before, Prayer restore can only be used once per",
                    "day, but it now restores half of your Prayer points.",
                ).also {
                    stage++
                }
            152 ->
                npc(
                    "The increased Farming experience is only available at",
                    "the allotments, flower and herb patches found just north",
                    "of Port Sarim.",
                ).also {
                    stage++
                }
            153 ->
                npc(
                    "As well as all of these features, the shield is pretty",
                    "handy in combat, and gives you a good Prayer boost.",
                ).also {
                    stage =
                        105
                }
            200 -> {
                if (Diary.hasClaimedLevelRewards(player!!, DiaryType.FALADOR, level)) {
                    npc("But you've already gotten yours!").also { stage = 105 }
                } else if (Diary.hasCompletedLevel(player!!, DiaryType.FALADOR, level)) {
                    npc("So, you've finished. Well done! I believe congratulations", "are in order.").also {
                        stage = 201
                    }
                } else {
                    npc("But you haven't finished!").also { stage = 105 }
                }
            }
            201 -> player("I believe rewards are in order.").also { stage++ }
            202 -> npc("Right you are.").also { stage++ }
            203 -> {
                npc(
                    "This is the second stage of the Falador shield: a kite",
                    "shield. It grants you all the benefits fo the buckler, but",
                    "with increased Prayer restore, and Farming experience",
                    "when using the patches near Falador.",
                )
                if (!Diary.hasClaimedLevelRewards(player!!, DiaryType.FALADOR, level)) {
                    Diary.flagRewarded(player!!, DiaryType.FALADOR, level)
                }
                stage = 204
            }
            204 ->
                npc(
                    "As before, Prayer restore can only be used once per",
                    "day, but it now restores half of your Prayer points.",
                ).also {
                    stage++
                }
            205 ->
                npc(
                    "The increased Farming experience is only available at",
                    "the allotments, flower and herb patches found just north",
                    "of Port Sarim.",
                ).also {
                    stage++
                }
            206 ->
                npc(
                    "As well as all of these features, the shield is pretty",
                    "handy in combat, and gives you a good Prayer boost.",
                ).also {
                    stage++
                }
            207 ->
                npc(
                    "I've even thrown in an old lamp I found. Try as I",
                    "might, I can't fill it with fuel or get it to light.",
                ).also {
                    stage++
                }
            208 -> player(FaceAnim.AMAZED, "Wow, thanks!").also { stage++ }
            209 -> npc("If you should lose your shield, come back and see me", "for another one.").also { stage = 105 }
            220 -> npc("Keep it up!").also { stage = 105 }
            999 -> end()
        }
    }
}
