package content.region.desert.dialogue.pollnivneach

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BanditDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc(FaceAnim.ANNOYED, "Go away.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BanditDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BANDIT_6388)
    }
}
