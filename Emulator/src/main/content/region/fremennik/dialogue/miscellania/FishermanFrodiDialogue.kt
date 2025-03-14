package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.DialogueFile
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class FishermanFrodiDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FISHERMAN_FRODI_1397)
        when (stage) {
            0 -> npc("Hello.").also { stage = END_DIALOGUE }
        }
    }
}
