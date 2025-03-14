package content.region.kandarin.dialogue.stronghold

import content.region.kandarin.quest.grandtree.dialogue.FemiGTDialogue
import core.api.face
import core.api.findLocalNPC
import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import org.rs.consts.NPCs

class GnomeGateGuardDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.GNOME_GUARD_163)
        when (stage) {
            0 -> npcl(FaceAnim.OLD_DEFAULT, "I'm afraid that we have orders not to let you in.").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Orders from who?").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "The head tree guardian, he say's you're a spy!").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "Glough!").also { stage++ }
            4 -> npc(FaceAnim.OLD_DEFAULT, "I'm sorry but you'll have to leave.").also { stage++ }
            5 -> {
                end()
                findLocalNPC(player!!, NPCs.FEMI_676)?.let {
                    face(player!!, it, 2)
                    openDialogue(player!!, FemiGTDialogue())
                }
            }
        }
    }
}
