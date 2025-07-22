package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.falador.quest.rd.plugin.SirTinleyPlugin
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Sir Tinley dialogue.
 */
@Initializable
class SirTinleyDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirTinleyPlugin(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_TINLEY_2286)
}
