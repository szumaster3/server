package content.global.ame.freakyforest

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class FreakyForesterDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (removeItem(player!!, Items.RAW_PHEASANT_6179) &&
            !getAttribute(player!!, GameAttributes.RE_FREAK_COMPLETE, false)
        ) {
            npcl(FaceAnim.NEUTRAL, "That's not the right one.").also { stage = END_DIALOGUE }
            setAttribute(player!!, GameAttributes.RE_FREAK_KILLS, false)
        } else if (removeItem(player!!, Items.RAW_PHEASANT_6178) ||
            getAttribute(player!!, GameAttributes.RE_FREAK_COMPLETE, false)
        ) {
            npcl(FaceAnim.NEUTRAL, "Thanks, ${player!!.username}, you may leave the area now.").also {
                stage = END_DIALOGUE
            }
            sendChat(
                findNPC(FreakyForesterUtils.FREAK_NPC)!!,
                "Thanks, ${player!!.username}, you may leave the area now.",
            )
            setAttribute(player!!, GameAttributes.RE_FREAK_COMPLETE, true)
        } else {
            when (getAttribute(player!!, GameAttributes.RE_FREAK_TASK, -1)) {
                NPCs.PHEASANT_2459 ->
                    sendNPCDialogue(
                        player!!,
                        FreakyForesterUtils.FREAK_NPC,
                        "Hey there ${player!!.username}. Can you kill the one tailed pheasant please. Bring me the raw pheasant when you're done.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                NPCs.PHEASANT_2460 ->
                    sendNPCDialogue(
                        player!!,
                        FreakyForesterUtils.FREAK_NPC,
                        "Hey there ${player!!.username}. Can you kill the two tailed pheasant please. Bring me the raw pheasant when you're done.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                NPCs.PHEASANT_2461 ->
                    sendNPCDialogue(
                        player!!,
                        FreakyForesterUtils.FREAK_NPC,
                        "Hey there ${player!!.username}. Can you kill the three tailed pheasant please. Bring me the raw pheasant when you're done.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                NPCs.PHEASANT_2462 ->
                    sendNPCDialogue(
                        player!!,
                        FreakyForesterUtils.FREAK_NPC,
                        "Hey there ${player!!.username}. Can you kill the four tailed pheasant please. Bring me the raw pheasant when you're done.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
            }
        }
    }
}
