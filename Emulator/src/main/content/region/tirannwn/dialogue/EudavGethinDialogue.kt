package content.region.tirannwn.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EudavGethinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Can I help you at all?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please. What are you selling?", "No thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, npc.id)
                    }
                    2 -> player(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return EudavGethinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EUDAV_2352, NPCs.GETHIN_2357)
    }
}
