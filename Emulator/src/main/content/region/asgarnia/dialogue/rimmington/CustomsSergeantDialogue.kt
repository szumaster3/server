package content.region.asgarnia.dialogue.rimmington

import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class CustomsSergeantDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.CUSTOMS_SERGEANT_7831)
        when (stage) {
            0 ->
                if (getQuestStage(player!!, "Rocking Out") < 1) {
                    sendNPCDialogue(
                        player!!,
                        NPCs.CUSTOMS_SERGEANT_7831,
                        "Zzzzzzzzzzzzzzzzzzz.",
                        FaceAnim.SLEEPING,
                    ).also { stage++ }
                } else {
                    sendDialogue(player!!, "Customs Sergeant seems too busy to talk.").also { stage = END_DIALOGUE }
                }
            1 -> player(FaceAnim.STRUGGLE, "Ahem.").also { stage++ }
            2 -> npc("Push off, I'm busy.").also { stage++ }
            3 -> player("Okay.").also { stage++ }
            4 -> npc(FaceAnim.ANNOYED, "Now!").also { stage = END_DIALOGUE }
        }
    }
}
