package content.region.asgarnia.dialogue.dwarvenmines

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class DrokarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello, how are you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Packages, packages and more!").also { stage++ }
            1 -> player(FaceAnim.OLD_DEFAULT, "Ugh.. Okay, have a good day.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DrokarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRORKAR_7723)
    }
}
