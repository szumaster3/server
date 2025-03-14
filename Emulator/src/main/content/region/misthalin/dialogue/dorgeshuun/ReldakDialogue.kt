package content.region.misthalin.dialogue.dorgeshuun

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ReldakDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_NORMAL, "Do you want to buy some leather armour?").also { stage++ }
            1 -> options("Yes please.", "No thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes please.").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "No thanks.").also { stage = 4 }
                }
            3 -> {
                end()
                openNpcShop(player, NPCs.RELDAK_5780)
            }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Have a good day!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ReldakDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RELDAK_5780)
    }
}
