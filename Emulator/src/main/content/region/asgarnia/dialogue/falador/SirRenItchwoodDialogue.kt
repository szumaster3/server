package content.region.asgarnia.dialogue.falador

import content.region.asgarnia.quest.rd.handlers.SirReenItchoodPuzzleListener
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SirRenItchwoodDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, SirReenItchoodPuzzleListener(), npc)
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SirRenItchwoodDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_REN_ITCHOOD_2287)
}
