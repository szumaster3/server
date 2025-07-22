package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.falador.quest.rd.plugin.SirKuamPlugin
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Sir Kuam Ferentse dialogue.
 */
@Initializable
class SirKuamFerentseDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SirKuamPlugin(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_KUAM_FERENTSE_2284)
}
