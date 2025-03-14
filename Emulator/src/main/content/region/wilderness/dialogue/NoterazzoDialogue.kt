package content.region.wilderness.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class NoterazzoDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_ASKING, "Hey, wanna trade? I'll give the best deals you can find.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes please.", "No thanks.", "How can you afford to give such good deals?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.NOTERAZZO_597)
                    }
                    2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                    3 -> player(FaceAnim.ASKING, "How can you afford to give such good deals?").also { stage++ }
                }
            2 -> npc("The general stores in Asgarnia and Misthalin are heavily", "taxed.").also { stage++ }
            3 ->
                npc(
                    "It really makes it hard for them to run an effective",
                    "business. For some reason taxmen don't visit my store.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return NoterazzoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NOTERAZZO_597)
    }
}
