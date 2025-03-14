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
class OronwenDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Hello, can I help?")
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
                    1 -> player(FaceAnim.FRIENDLY, "Yes please. What are you selling?").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
                }

            2 -> {
                end()
                openNpcShop(player, NPCs.ORONWEN_2353)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return OronwenDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ORONWEN_2353)
    }
}
