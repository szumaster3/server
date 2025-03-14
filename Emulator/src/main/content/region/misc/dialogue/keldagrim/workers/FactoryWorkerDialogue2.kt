package content.region.misc.dialogue.keldagrim.workers

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FactoryWorkerDialogue2(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Are you okay?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_ANGRY1, "Don't I look okay?").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "If you were any shorter you wouldn't exist.").also { stage++ }
            3 -> npcl(FaceAnim.OLD_ANGRY1, "Very funny, human.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_WORKER_2173)
    }
}
