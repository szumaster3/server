package content.region.misthalin.varrock.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Gate guard (Varrock) dialogue.
 */
@Initializable
class VarrockGateGuardDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Please don't disturb me, I've got to keep an eye out for", "suspicious individuals.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    override fun newInstance(player: Player?): Dialogue = VarrockGateGuardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GUARD_368)
}
