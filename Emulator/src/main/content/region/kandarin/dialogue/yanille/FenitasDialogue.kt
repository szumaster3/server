package content.region.kandarin.dialogue.yanille

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class FenitasDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Would you like to buy some cooking equipment?").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HAPPY, "Yes please.").also { stage++ }
            1 -> {
                end()
                openNpcShop(player, NPCs.FRENITA_593)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FenitasDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRENITA_593)
    }
}
