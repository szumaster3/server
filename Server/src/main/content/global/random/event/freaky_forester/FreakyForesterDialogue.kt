package content.global.random.event.freaky_forester

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Freaky forester random event dialogue.
 * @author szu
 */
class FreakyForesterDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        val player = player ?: return
        val task = getAttribute(player, GameAttributes.RE_FREAK_TASK, -1)
        val username = player.username

        when {
            removeItem(player, Items.RAW_PHEASANT_6179) && !getAttribute(player, GameAttributes.RE_FREAK_COMPLETE, false) -> {
                npcl(FaceAnim.NEUTRAL, "That's not the right one.")
                setAttribute(player, GameAttributes.RE_FREAK_KILLS, false)
                stage = END_DIALOGUE
            }

            removeItem(player, Items.RAW_PHEASANT_6178) || getAttribute(player, GameAttributes.RE_FREAK_COMPLETE, false) -> {
                val message = "Thanks, $username, you may leave the area now."
                npcl(FaceAnim.NEUTRAL, message)
                sendChat(findNPC(FreakyForesterUtils.FREAK_NPC)!!, message)
                setAttribute(player, GameAttributes.RE_FREAK_COMPLETE, true)
                stage = END_DIALOGUE
            }

            else -> {
                val tails = when (task) {
                    NPCs.PHEASANT_2459 -> 1
                    NPCs.PHEASANT_2460 -> 2
                    NPCs.PHEASANT_2461 -> 3
                    NPCs.PHEASANT_2462 -> 4
                    else -> null
                }

                tails?.let {
                    val message = "Hey there $username. Can you kill the $it tailed pheasant please. Bring me the raw pheasant when you're done."
                    sendNPCDialogue(player, FreakyForesterUtils.FREAK_NPC, message)
                    stage = END_DIALOGUE
                }
            }
        }
    }
}
