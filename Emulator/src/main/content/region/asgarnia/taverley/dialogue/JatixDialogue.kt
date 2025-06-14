package content.region.asgarnia.taverley.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class JatixDialogue(player: Player? = null) : Dialogue(player) {

    override fun newInstance(player: Player?): Dialogue = JatixDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello, how can I help you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 ->
                options(
                    "What are you selling?",
                    "You can't; I'm beyond help.",
                    "I'm okay, thank you.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, NPCs.JATIX_587) }
                    2 -> player(FaceAnim.NEUTRAL, "You can't; I'm beyond help.").also { stage = END_DIALOGUE }
                    3 -> player("I'm okay, thank you.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JATIX_587)
}
