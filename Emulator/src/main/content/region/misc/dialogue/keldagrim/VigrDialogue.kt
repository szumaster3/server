package content.region.misc.dialogue.keldagrim

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class VigrDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "What do you want, human?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_ASKING, "Ehm, anything on offer?").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "Can you wield a warhammer?", "If not, then go away.").also { stage++ }
            2 -> options("Of course I can.", "I can, but I won't.").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Of course I can.").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "I can, but I won't.").also { stage = END_DIALOGUE }
                }

            4 -> {
                end()
                openNpcShop(player, NPCs.VIGR_2151)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return VigrDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VIGR_2151)
    }
}
