package content.region.kandarin.quest.grail.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class HealthyPeasantDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.PEASANT_215)

        when (stage) {
            0 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Oh happy day! Suddenly our crops are growing again! It'll be a bumper harvest this year!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
    }
}
