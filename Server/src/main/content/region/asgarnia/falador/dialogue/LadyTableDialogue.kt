package content.region.asgarnia.falador.dialogue

import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import shared.consts.NPCs

/**
 * Represents the Lady Table dialogue.
 */
@Initializable
class LadyTableDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, content.region.asgarnia.falador.quest.rd.plugin.LadyTableDialogue(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.LADY_TABLE_2283)
}
