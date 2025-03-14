package content.region.kandarin.dialogue.ardougne

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SpiceSellerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Are you interested in buying or selling spice?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes.", "No.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.SPICE_SELLER_572)
                    }

                    2 -> player(FaceAnim.HALF_GUILTY, "No, thanks.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return SpiceSellerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPICE_SELLER_572)
    }
}
