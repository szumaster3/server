package content.region.misc.dialogue.keldagrim.library

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CustomerDwarfDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello, can you help me?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Ssshhh, don't talk...").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "But I just want to ask you something.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Sssshhhh! Can't you read the sign?").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "What sign??").also { stage++ }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Don't be a cheeky human!").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "Ssshhhh.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CustomerDwarfDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CUSTOMER_2167)
    }
}
