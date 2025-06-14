package content.region.misthalin.lumbridge.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class JimmyTheChiselDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Hello mate!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    override fun getIds(): IntArray = intArrayOf(NPCs.JIMMY_THE_CHISEL_1718)
}
