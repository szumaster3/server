package content.region.kandarin.quest.merlin.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.world.repository.Repository.findNPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class SirMordredDialogueFile : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.SIR_MORDRED_247)

        when (stage) {
            0 -> npcl(FaceAnim.ANGRY, "You DARE to invade MY stronghold?!?! Have at thee knave!!!").also { stage++ }

            1 -> {
                end()
                val mord = findNPC(NPCs.SIR_MORDRED_247)

                if (mord != null) {
                    mord.attack(player!!)
                }
                stage = END_DIALOGUE
            }
        }
    }
}
