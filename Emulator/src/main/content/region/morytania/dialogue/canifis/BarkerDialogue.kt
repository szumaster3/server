package content.region.morytania.dialogue.canifis

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BarkerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HAPPY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HAPPY,
                    "You are looking for clothes, yes? You look at my",
                    "products! I have very many nice clothes, yes?",
                ).also {
                    stage++
                }
            1 -> options("Yes, please.", "No, thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Yes, please.").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "No thanks.").also { stage = 4 }
                }
            3 -> {
                end()
                openNpcShop(player, NPCs.BARKER_1039)
            }
            4 ->
                npc(FaceAnim.HALF_GUILTY, "Unfortunate for you, yes?", "Many bargains, won't find elsewhere!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BarkerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARKER_1039)
    }
}
