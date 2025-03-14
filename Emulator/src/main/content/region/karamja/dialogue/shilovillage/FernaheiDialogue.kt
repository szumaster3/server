package content.region.karamja.dialogue.shilovillage

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FernaheiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Welcome to Fernahei's Fishing Shop Bwana!", "Would you like to see my items?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please!", "No, but thanks for the offer.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Yes, please.").also { stage = 10 }
                    2 -> player(FaceAnim.FRIENDLY, "No, but thanks for the offer.").also { stage = 20 }
                }

            10 -> {
                end()
                openNpcShop(player, NPCs.FERNAHEI_517)
            }

            20 -> npc(FaceAnim.FRIENDLY, "That's fine, and thanks for your interest.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FERNAHEI_517)
    }
}
