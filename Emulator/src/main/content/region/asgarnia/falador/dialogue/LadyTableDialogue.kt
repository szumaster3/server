package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.falador.quest.rd.plugin.LadyTablePuzzleListener
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
        openDialogue(player, content.region.asgarnia.falador.quest.rd.plugin.LadyTablePuzzleListener(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LADY_TABLE_2283)
}
