package content.minigame.ratpits.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class VaeringkDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.VAERINGK_2992)
        when (stage) {
            0 -> player("Hello. Shouldn't you be on guard duty?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.OLD_DRUNK_RIGHT,
                    "In this state? I've had too many beers s'already, beside' s'i'm off duty.",
                ).also { stage++ }

            2 -> npcl(FaceAnim.OLD_DEFAULT, "The commander 's'given me the day off.").also { stage = END_DIALOGUE }
        }
    }
}
