package content.region.asgarnia.falador.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the White Knight (Falador party room) dialogue.
 */
@Initializable
class WhiteKnightDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        sendDialogue(player, "He is too busy dancing to talk!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean = true

    override fun getIds(): IntArray = intArrayOf(NPCs.KNIGHT_660)
}
