package content.region.misthalin.lumbridge.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SethGroatsDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "M'arnin'... going to milk me cowsies!").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean = true

    override fun getIds(): IntArray = intArrayOf(NPCs.SETH_GROATS_452)
}
