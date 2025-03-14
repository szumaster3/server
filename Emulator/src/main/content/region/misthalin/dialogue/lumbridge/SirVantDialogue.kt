package content.region.misthalin.dialogue.lumbridge

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SirVantDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        sendDialogue(player, "Sir Vant seems too busy to talk.").also { stage = END_DIALOGUE }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_VANT_7942)
    }
}
