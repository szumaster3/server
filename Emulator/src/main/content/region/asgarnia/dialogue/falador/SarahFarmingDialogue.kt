package content.region.asgarnia.dialogue.falador

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SarahFarmingDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Hi!").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "Would you like to see what I have in stock?").also { stage++ }
            2 -> options("Yes please.", "No, thank you.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Yes please.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "No, thank you.").also { stage = END_DIALOGUE }
                }
            4 -> {
                end()
                openNpcShop(player, NPCs.SARAH_2304)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SARAH_2304)
    }
}
