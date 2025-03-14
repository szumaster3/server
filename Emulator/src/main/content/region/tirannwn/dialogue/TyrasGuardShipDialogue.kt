package content.region.tirannwn.dialogue

import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TyrasGuardShipDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        sendDialogue(player, "General seems too busy to talk.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TyrasGuardShipDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TYRAS_GUARD_4649)
    }
}
