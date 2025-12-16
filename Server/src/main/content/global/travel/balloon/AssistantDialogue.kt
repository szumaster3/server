package content.global.travel.balloon

import core.api.openInterface
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.NPCs

/**
 * Represents the assistant dialogue.
 */
class AssistantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val faceExpression = if (npc!!.id != NPCs.ASSISTANT_LE_SMITH_5056) FaceAnim.HALF_GUILTY else FaceAnim.OLD_NORMAL
        when (stage) {
            0 -> npcl(faceExpression, "Do you want to use the balloon? Just so you know, some locations require special logs and high Firemaking skills.").also { stage++ }
            1 -> showTopics(
                Topic("Yes.", 2, true),
                Topic("No.", END_DIALOGUE),
                Topic("Who are you?", npc?.id),
            )
            2 -> {
                end()
                openInterface(player!!, Components.ZEP_BALLOON_MAP_469)
            }
            3 -> npcl(faceExpression, "Do you want to use the balloon?").also { stage = 1 }
            4 -> playerl(FaceAnim.ASKING, "Why?").also { stage++ }
            5 -> npcl(FaceAnim.OLD_LAUGH1, "They said I was too full of hot air.").also { stage = 3 }

            NPCs.ASSISTANT_SERF_5053 -> npcl(faceExpression, "I am a Serf. Assistant Serf to you! Auguste freed me and gave me this job.").also { stage = 3 }
            NPCs.ASSISTANT_MARROW_5055 -> npcl(faceExpression, "I am Assistant Marrow. I'm working here part time while I study to be a doctor.").also { stage = 3 }
            NPCs.ASSISTANT_LE_SMITH_5056 -> npcl(faceExpression, "I am Assistant Le Smith. I used to work as a glider pilot, but they kicked me off.").also { stage = 4 }
            NPCs.ASSISTANT_STAN_5057 -> npcl(faceExpression, "I am Stan. Auguste hired me to look after this balloon. I make sure people are prepared to fly.").also { stage = 3 }
            NPCs.ASSISTANT_BROCK_5054 -> npcl(faceExpression, "I am Assistant Brock. I serve under Auguste as his number two assistant.").also { stage = 3 }
        }
    }
}