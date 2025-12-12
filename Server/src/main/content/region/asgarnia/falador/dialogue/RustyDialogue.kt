package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Rusty dialogue.
 */
class RustyDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npc(FaceAnim.HALF_ASKING, "Hiya. Are you carrying anything valuable?").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "Why are you asking?").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "Um... It's a quiz. I'm asking everyone I meet if they're", "carrying anything valuable.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "What would you do if I said I had loads of expensive items", "with me?").also { stage++ }
            4 -> npc(FaceAnim.HALF_THINKING, "Ooh, do you? It's been ages since anyone said they'd got", "anything worth stealing.").also { stage++ }
            5 -> player(FaceAnim.HALF_ASKING, "'Anything worth stealing'?").also { stage++ }
            6 -> npc(FaceAnim.STRUGGLE, "Um... Not that I'd dream of stealing anything!").also { stage++ }
            7 -> player(FaceAnim.HALF_GUILTY, "Well, I'll say I'm not carrying anything valuable at all.").also { stage++ }
            8 -> npc(FaceAnim.ANGRY, "Oh, what a shame.").also { stage = END_DIALOGUE }
        }
    }
}
