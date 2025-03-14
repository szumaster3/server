package content.region.asgarnia.dialogue.entrana

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FrincosDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello, how can I help you?")
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
                    "You can't; I'm beyond help.",
                    "I'm okay, thank you.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What are you selling?").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "You can't; I'm beyond help.").also { stage = END_DIALOGUE }
                    3 -> player(FaceAnim.HALF_GUILTY, "I'm okay, thank you.").also { stage = END_DIALOGUE }
                }
            2 -> {
                end()
                openNpcShop(player, npc.id)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FrincosDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FRINCOS_578)
    }
}
