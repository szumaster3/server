package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.quest.rd.plugin.SirSpishyusDialogueFile
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

@Initializable
class SirSpishyusDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        openDialogue(player, SirSpishyusDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SIR_SPISHYUS_2282)
}
