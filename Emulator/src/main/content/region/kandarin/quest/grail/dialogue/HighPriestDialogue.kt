package content.region.kandarin.quest.grail.dialogue

import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class HighPriestDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.HIGH_PRIEST_216)

        when (stage) {
            0 -> npcl(FaceAnim.NEUTRAL, "Many greetings. Welcome to our fair island.").also { stage++ }
            1 -> playerl(FaceAnim.NEUTRAL, "Hello, I am in search of the Holy Grail.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The object of which you speak did once pass through holy Entrana. I know not where it is now however.",
                ).also {
                    stage++
                }

            3 -> npcl(FaceAnim.NEUTRAL, "Nor do I really care.").also { stage++ }
            4 -> {
                openDialogue(player!!, CroneDialogue(true), NPCs.CRONE_217)
            }
        }
    }
}
