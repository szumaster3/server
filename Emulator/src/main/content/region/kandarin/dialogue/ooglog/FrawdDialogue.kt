package content.region.kandarin.dialogue.ooglog

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FrawdDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.CHILD_NORMAL, "What you want, human?").also { stage++ }
            1 -> options("So what do you have for sale, then?", "Never mind.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "So what do you have for sale, then?").also { stage = 3 }
                    2 -> playerl(FaceAnim.FRIENDLY, "Never mind.").also { stage = END_DIALOGUE }
                }

            3 -> {
                end()
                openNpcShop(player, NPCs.FRAWD_7048)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return FrawdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRAWD_7048)
    }
}
