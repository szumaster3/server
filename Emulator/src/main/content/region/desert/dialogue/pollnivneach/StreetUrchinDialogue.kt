package content.region.desert.dialogue.pollnivneach

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class StreetUrchinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        sendDialogue(player, "This child doesn't seem interested in you.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return StreetUrchinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STREET_URCHIN_6357)
    }
}
