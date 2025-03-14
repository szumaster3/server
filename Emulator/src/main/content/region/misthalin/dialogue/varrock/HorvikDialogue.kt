package content.region.misthalin.dialogue.varrock

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HorvikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Hello, do you need any help?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Do you want to trade?", "No, thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.HORVIK_549)
                    }

                    2 -> player(FaceAnim.FRIENDLY, "No, thanks. I'm just looking around.").also { stage++ }
                }

            2 ->
                npcl(FaceAnim.HAPPY, "Well, come and see me if you're ever in need of armour!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return HorvikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HORVIK_549)
    }
}
