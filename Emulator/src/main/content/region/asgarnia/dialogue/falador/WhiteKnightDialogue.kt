package content.region.asgarnia.dialogue.falador

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class WhiteKnightDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        sendDialogue(player, "He is too busy dancing to talk!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KNIGHT_660)
    }
}
