package content.region.asgarnia.dialogue.portsarim

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PortSarimBrianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("So, are you selling something?", "'Ello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "So, are you selling something?").also { stage++ }
                    2 -> player(FaceAnim.HAPPY, "'Ello.").also { stage = 3 }
                }
            1 -> npc(FaceAnim.HAPPY, "Yep, take a look at these great axes.").also { stage++ }
            2 -> end().also { openNpcShop(player, npc.id) }
            3 -> npc(FaceAnim.HAPPY, "'Ello.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRIAN_559)
    }
}
