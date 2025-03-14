package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class UlifedDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Say, aren't you the human who got arrested here the other day, by the boat?",
                ).also {
                    stage++
                }
            1 -> playerl(FaceAnim.FRIENDLY, "Yes, but we cleared up the whole situation.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_DEFAULT, "Right, right.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return UlifedDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ULIFED_2193)
    }
}
