package content.region.karamja.quest.mm.dialogue

import core.api.rewardXP
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.skill.Skills

class DaeroTrainingPostQuestDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Good day, High Tree Guardian.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Hello there. I hear your mission is complete.").also { stage++ }
            2 ->
                playerl(
                    "News travels quickly on this tree. I expect you also know that I am to be enrolled in the Royal Guard training programme.",
                ).also {
                    stage++
                }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "Indeed I do.").also { stage++ }
            4 -> playerl("How long does it take?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "For you, it should hardly take any time at all.").also { stage++ }
            6 -> playerl("Then let us begin.").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Enthusiasm. I like that. We will first begin with a series of exercises designed to increase your strength and stamina.",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "We then will follow these up by improving your attack and defence techniques.",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You will improve your strength, stamina, attack and defence, but you may choose what you want to focus on.",
                ).also { stage++ }

            10 ->
                options(
                    "Focus on increasing strength and stamina...",
                    "Focus on improving attack and defence techniques...",
                ).also { stage++ }

            11 ->
                when (buttonID) {
                    1 -> stage = 30
                    2 -> stage = 40
                }

            30 -> {
                rewardXP(player!!, Skills.ATTACK, 20000.0)
                rewardXP(player!!, Skills.DEFENCE, 20000.0)
                rewardXP(player!!, Skills.STRENGTH, 35000.0)
                rewardXP(player!!, Skills.HITPOINTS, 35000.0)
                setAttribute(player!!, "/save:mm:xp_reward", true)
                stage = 99
            }

            40 -> {
                rewardXP(player!!, Skills.ATTACK, 35000.0)
                rewardXP(player!!, Skills.DEFENCE, 35000.0)
                rewardXP(player!!, Skills.STRENGTH, 20000.0)
                rewardXP(player!!, Skills.HITPOINTS, 20000.0)
                setAttribute(player!!, "/save:mm:xp_reward", true)
                stage = 99
            }

            99 -> end()
        }
    }
}
