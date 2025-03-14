package content.region.asgarnia.dialogue.portsarim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class ThakiTheDeliveryDwarfDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Arrr!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HAPPY, "Hi little fellow.").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "What did you just say to me!?").also { stage++ }
            2 -> player(FaceAnim.GUILTY, "Arrr! nothing, nothing at all..").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THAKI_THE_DELIVERY_DWARF_7115)
    }
}
