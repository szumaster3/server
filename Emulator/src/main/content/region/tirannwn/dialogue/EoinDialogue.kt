package content.region.tirannwn.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class EoinDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(FaceAnim.FRIENDLY, "Sorry, I cannot stop or Iona will catch me, we are playing tag!").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return EoinDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EOIN_2368)
    }
}
