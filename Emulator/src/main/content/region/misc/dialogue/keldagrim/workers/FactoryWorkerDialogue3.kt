package content.region.misc.dialogue.keldagrim.workers

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FactoryWorkerDialogue3(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "What are you dwarves doing in this factory?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_ANGRY1, "Working of course, can't you see that?").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "But working on what?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Refining the ore that is being brought into the factory, of course.",
                ).also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "And what does that mean?").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.OLD_ANGRY1,
                    "It means you should stop asking so many questions and get back to work!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_WORKER_2174)
    }
}
