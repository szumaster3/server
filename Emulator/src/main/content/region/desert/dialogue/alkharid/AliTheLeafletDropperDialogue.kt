package content.region.desert.dialogue.alkharid

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

class AliTheLeafletDropperDialogue(
    val it: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (it) {
            1 ->
                when (stage) {
                    0 -> npcl(FaceAnim.CHILD_NORMAL, "Here! Take one and let me get back to work.").also { stage++ }
                    1 ->
                        npcl(
                            FaceAnim.CHILD_THINKING,
                            "I still have hundreds of these flyers to hand out. I wonder if Ali would notice if I quietly dumped them somewhere?",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
            2 ->
                npcl(
                    FaceAnim.CHILD_SUSPICIOUS,
                    "Are you trying to be funny or has age turned your brain to mush? You already have a flyer!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
