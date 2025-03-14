package content.region.karamja.dialogue

import content.region.karamja.quest.mm.dialogue.GLOCaranockDialogue
import core.api.openDialogue
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CaranockDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (player.location.regionId == 11823) {
            openDialogue(player, GLOCaranockDialogue(), npc)
            return true
        }
        sendDialogue(player, "Caranock seems too busy to talk.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GLO_CARANOCK_1427)
    }
}
