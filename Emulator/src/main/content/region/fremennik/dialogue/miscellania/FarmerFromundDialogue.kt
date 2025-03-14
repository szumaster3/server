package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class FarmerFromundDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FARMER_FROMUND_3917)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hey! This is the Queen's farm. You'll need her approval to make use of it.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
