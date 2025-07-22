package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.falador.quest.rd.plugin.HynnTerprettPlugin
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Ms Hynn Terprett dialogue.
 */
@Initializable
class MsHynnTerprettDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, HynnTerprettPlugin(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MS_HYNN_TERPRETT_2289)
}
