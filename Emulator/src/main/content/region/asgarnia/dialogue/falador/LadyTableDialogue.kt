package content.region.asgarnia.dialogue.falador

import content.region.asgarnia.quest.rd.handlers.tests.ObservationTest
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class LadyTableDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, ObservationTest(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LADY_TABLE_2283)
}
