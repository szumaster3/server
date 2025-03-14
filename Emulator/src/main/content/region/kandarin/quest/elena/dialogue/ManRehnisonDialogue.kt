package content.region.kandarin.quest.elena.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

class ManRehnisonDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "We don't have good days here anymore. Curse King Tyras.").also { stage++ }
            1 ->
                options(
                    "Oh okay, bad day then.",
                    "Why, what has he done?",
                    "I'm looking for a woman called Elena.",
                ).also { stage++ }

            2 ->
                when (buttonID) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Oh okay, bad day then.").also { stage = END_DIALOGUE }
                    2 -> playerl(FaceAnim.FRIENDLY, "Why, what has he done?").also { stage = 3 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I'm looking for a woman called Elena.").also { stage = 4 }
                }

            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "His army curses our city with this plague then wanders off again, leaving us to clear up the pieces.",
                ).also { stage = END_DIALOGUE }

            4 -> npcl(FaceAnim.THINKING, "Not heard of her.").also { stage = END_DIALOGUE }
        }
    }
}
