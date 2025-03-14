package content.region.kandarin.dialogue.ardougne

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class GemMerchantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Here, look at my lovely gems.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                end()
                openNpcShop(player, NPCs.GEM_MERCHANT_570)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GemMerchantDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GEM_MERCHANT_570)
    }
}
