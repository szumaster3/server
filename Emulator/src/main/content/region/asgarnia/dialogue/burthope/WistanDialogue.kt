package content.region.asgarnia.dialogue.burthope

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Wistan dialogue.
 */
@Initializable
class WistanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FaceAnim.FRIENDLY, "Hi!").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Welcome to Burthorpe Supplies. Your last shop before heading north into the mountains!",
                ).also {
                    stage++
                }
            2 -> npcl(FaceAnim.FRIENDLY, "Would you like to buy something?").also { stage++ }
            3 -> options("Yes, please.", "No, thanks.").also { stage++ }
            4 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, please.").also { stage = 5 }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
                }
            5 -> {
                end()
                openNpcShop(player, npc.id)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WISTAN_1083)
    }
}
