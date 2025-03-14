package content.region.kandarin.dialogue.catherby

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class VanessaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Hello. How can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What are you selling?",
                    "Can you give me any Farming advice?",
                    "I'm okay, thank you.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, npc.id) }
                    2 -> player(FaceAnim.HALF_ASKING, "Can you give me any Farming advice?").also { stage++ }
                    3 -> player(FaceAnim.FRIENDLY, "I'm okay, thank you.").also { stage = END_DIALOGUE }
                }

            2 -> npc(FaceAnim.HALF_GUILTY, "Yes - ask a gardener.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return VanessaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.VANESSA_2305)
    }
}
