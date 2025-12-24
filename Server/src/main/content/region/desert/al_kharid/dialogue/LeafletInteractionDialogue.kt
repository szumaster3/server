package content.region.desert.al_kharid.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.tools.END_DIALOGUE

class LeafletInteractionDialogue(var npcId: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> player(FaceAnim.NEUTRAL, "Hi. I have this money off voucher.").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "So I see. Unfortunately, it seems to have expired...", "yesterday. Never mind.").also { stage++ }
            2 -> player(FaceAnim.HALF_CRYING, "But I only just got it!").also { stage++ }
            3 -> npcl(FaceAnim.HALF_GUILTY, "I'm sorry. There's nothing I can do. Goodbye.").also { stage = END_DIALOGUE }
        }
    }
}