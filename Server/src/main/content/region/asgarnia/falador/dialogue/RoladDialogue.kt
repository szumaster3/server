package content.region.asgarnia.falador.dialogue

import core.api.hasRequirement
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE
import shared.consts.Quests

/**
 * Represents the Rolad dialogue.
 */
class RoladDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> if(!hasRequirement(player!!, Quests.BETWEEN_A_ROCK, false)) {
                npc(FaceAnim.OLD_NORMAL, "Oh, hello... do I know you?").also { stage++ }
            } else {
                npc(FaceAnim.OLD_NORMAL, "Can you leave me alone please? I'm trying to study.").also { stage = END_DIALOGUE }
            }
            1 -> player(FaceAnim.HALF_ASKING, "Ehm... well... my name is " + player?.username + ", if that rings any bell?").also { stage++ }
            2 -> npc(FaceAnim.OLD_NORMAL, "No, never heard of you.").also { stage = END_DIALOGUE }
        }
    }
}
