package content.region.fremennik.dialogue.miscellania

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FinnDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Can I help you, your Royal Highness?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Yes please. What are you selling?",
                    "No thanks.",
                    "What's it like living down here?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.ASKING, "Yes please. What are you selling?").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "No thanks.").also { stage = END_DIALOGUE }
                    3 -> player(FaceAnim.ASKING, "What's it like living down here?").also { stage = 3 }
                }
            2 -> {
                end()
                openNpcShop(player, NPCs.FINN_3922)
            }
            3 ->
                npc(FaceAnim.HALF_WORRIED, "A lot drier in the winter than it is above ground.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FinnDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FINN_3922)
    }
}
