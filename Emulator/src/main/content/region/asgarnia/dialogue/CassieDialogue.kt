package content.region.asgarnia.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CassieDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "I buy and sell shields; do you want to trade?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, please.", "No, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Yes, please.").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
                }
            2 -> {
                end()
                openNpcShop(player, NPCs.CASSIE_577)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CassieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CASSIE_577)
    }
}
