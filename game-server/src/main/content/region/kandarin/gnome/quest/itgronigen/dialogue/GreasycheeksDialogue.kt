package content.region.kandarin.gnome.quest.itgronigen.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GreasycheeksDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Shush! I'm concentrating.").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "Oh, sorry.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GreasycheeksDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GREASYCHEEKS_6127)
}
