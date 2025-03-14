package content.region.kandarin.dialogue.ooglog

import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

class GrimechinDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            START_DIALOGUE ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "So, how do you like what's, um, being done to your head?",
                ).also {
                    stage++
                }
            1 ->
                sendNPCDialogue(
                    player!!,
                    NPCs.KRINGK_7063,
                    "Quit talking to goblin, Player. If she moves her head, she mess up work.",
                    FaceAnim.CHILD_NEUTRAL,
                ).also {
                    stage++
                }
            2 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oh, sorry! I would never dream of interfering in the creative process.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
