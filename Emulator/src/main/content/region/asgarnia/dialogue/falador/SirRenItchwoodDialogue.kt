package content.region.asgarnia.dialogue.falador

import content.region.asgarnia.quest.rd.handlers.tests.DetailTest
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
        openDialogue(player, DetailTest(), npc)
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SirRenItchwoodDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_REN_ITCHOOD_2287)
    }
}
