package content.region.asgarnia.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class FaladorSquireDiaryDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        val replacementReward: Boolean = Diary.canReplaceReward(player!!, DiaryType.FALADOR, 2)
        npc = NPC(NPCs.SQUIRE_606)
        when (stage) {
            0 ->
                if (replacementReward) {
                    player("I seem to have lost my Falador shield...").also { stage = 80 }
                } else {
                    npc("How are you getting on with the Achievement Diary?").also { stage = 90 }
                }
            80 -> {
                Diary.grantReplacement(player!!, DiaryType.FALADOR, 2)
                npc("Here's your replacement. Please be more careful.").also { stage = END_DIALOGUE }
            }
            90 -> options("I've come for my reward.", "I'm doing good.", "I have a question.").also { stage++ }
            91 ->
                when (buttonID) {
                    1 -> player("I've come for my reward.").also { stage = 200 }
                    2 -> player("I'm doing good.").also { stage = 220 }
                    3 -> player("I have a question.").also { stage = 105 }
                }
            105 -> {
                if (!Diary.hasClaimedLevelRewards(player!!, DiaryType.FALADOR, 2)) {
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
                    4 -> player("See you later.").also { stage = END_DIALOGUE }
                }
            107 ->
                when (buttonID) {
                    1 -> player("Can you remind me what my Falador shield does, please?").also { stage = 150 }
                    2 -> player("What is the Achievement Diary?").also { stage = 110 }
                    3 -> player("What are the rewards?").also { stage = 120 }
                    4 -> player("How do I claim the rewards?").also { stage = 130 }
                    5 -> player("See you later.").also { stage = END_DIALOGUE }
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
                    "speak to Redbeard the Pirate in Port Sarim, The Chemist",
                    "in Rimmington, or myself.",
                ).also {
                    stage =
                        105
                }
            150 ->
                npc(
                    "This is the final stage of the Falador shield: a tower",
                    "shield. It grants you all the benefits fo the buckler",
                    "and kiteshield did, full Prayer restore, and access to",
                    "some interesting new seeds that my friend Wyson has",
                ).also {
                    stage++
                }
            151 -> npc("found.").also { stage++ }
            152 ->
                npc(
                    "As before, Prayer restore can only be used once per",
                    "day, but this time it restores all of your Prayer points!",
                ).also {
                    stage++
                }
            153 ->
                npc(
                    "The new seeds I mentioned were discovered by Wyson,",
                    "who can be found in Falador Park. He'll give you some",
                    "new seeds in return for the skin of the giant mole",
                    "beneath Falador Park..",
                ).also {
                    stage++
                }
            154 ->
                npc(
                    "He'll only offer them to you if you're wielding the",
                    "Falador shield, though.",
                ).also { stage++ }
            155 ->
                npc(
                    "As well as all these features, the shield is pretty handy",
                    "in combat, and gives you a big Prayer boost.",
                ).also {
                    stage =
                        105
                }
            200 -> {
                if (Diary.hasClaimedLevelRewards(player!!, DiaryType.FALADOR, 2)) {
                    npc("But you've already gotten yours!").also { stage = 105 }
                } else if (Diary.hasCompletedLevel(player!!, DiaryType.FALADOR, 2)) {
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
                Diary.flagRewarded(player!!, DiaryType.FALADOR, 2)
                npc(
                    "This is the final stage of the Falador shield: a tower",
                    "shield. It grants you all the benefits fo the buckler",
                    "and kiteshield did, full Prayer restore, and access to",
                    "some interesting new seeds that my friend Wyson has",
                ).also {
                    stage =
                        204
                }
            }
            204 -> npc("found.").also { stage++ }
            205 ->
                npc(
                    "As before, Prayer restore can only be used once per",
                    "day, but this time it restores all of your Prayer points!",
                ).also {
                    stage++
                }
            206 ->
                npc(
                    "The new seeds I mentioned were discovered by Wyson,",
                    "who can be found in Falador Park. He'll give you some",
                    "new seeds in return for the skin of the giant mole",
                    "beneath Falador Park..",
                ).also {
                    stage++
                }
            207 ->
                npc(
                    "He'll only offer them to you if you're wielding the",
                    "Falador shield, though.",
                ).also { stage++ }
            208 ->
                npc(
                    "As well as all these features, the shield is pretty handy",
                    "in combat, and gives you a big Prayer boost.",
                ).also {
                    stage++
                }
            209 -> player(FaceAnim.AMAZED, "Wow, thanks!").also { stage++ }
            210 -> npc("If you should lose your shield, come back and see me", "for another one.").also { stage = 105 }
            220 -> npc("Keep it up!").also { stage = 105 }
        }
    }
}
