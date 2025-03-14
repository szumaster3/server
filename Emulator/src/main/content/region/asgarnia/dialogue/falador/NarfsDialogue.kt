package content.region.asgarnia.dialogue.falador

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class NarfsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.HALF_GUILTY, "That's a funny name you've got.")
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
                    "'Narf'? You think that's funny?",
                    "At least I Don't call myself '" + player.username + "' ",
                    "Where did you get a name like that?",
                ).also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "It seemed like a good idea at the time!").also { stage++ }
            2 -> npc(FaceAnim.HALF_GUILTY, "Bah!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NARF_3238)
    }
}
