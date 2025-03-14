package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FaladorGardenerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Oi'm busy. If tha' wants owt, tha' can go find Wyson.",
                    "He's ta boss 'round here. And,",
                    "KEEP YE' TRAMPIN' FEET OFF MA'FLOWERS!",
                ).also {
                    stage++
                }
            1 -> player(FaceAnim.HALF_GUILTY, "Right...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GARDENER_1217, NPCs.GARDENER_3234)
    }
}
