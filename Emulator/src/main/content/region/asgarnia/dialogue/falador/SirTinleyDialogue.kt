package content.region.asgarnia.dialogue.falador

import content.region.asgarnia.quest.rd.handlers.SirTinleyPuzzleListener
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SirTinleyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, SirTinleyPuzzleListener(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_TINLEY_2286)
}
