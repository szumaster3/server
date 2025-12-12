package content.region.asgarnia.falador.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

/**
 * Represents the Drunken Man dialogue.
 */
class FaladorDrunkenManDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.FRIENDLY, "Hello.").also { stage++ }
            1 -> npc(FaceAnim.DRUNK, "... whassup?").also { stage++ }
            2 -> player(FaceAnim.ASKING, "Are you alright?").also { stage++ }
            3 -> npc(FaceAnim.DRUNK, "... see... two of you... why there two of you?").also { stage++ }
            4 -> player(FaceAnim.FRIENDLY, "There's only one of me, friend.").also { stage++ }
            5 -> npc(FaceAnim.DRUNK, "... no, two of you... you can't count...", "... maybe you drunk too much...").also { stage++ }
            6 -> player(FaceAnim.HALF_THINKING, "Whatever you say, friend.").also { stage++ }
            7 -> npc(FaceAnim.DRUNK, "... giant hairy cabbages...").also { stage = END_DIALOGUE }
        }
    }
}
