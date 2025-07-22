package content.region.asgarnia.falador.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Rolad dialogue.
 */
@Initializable
class RoladDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Oh, hello... do I know you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_ASKING, "Ehm... well... my name is " + player.username + ", if that rings any bell?").also { stage++ }
            1 -> npc(FaceAnim.OLD_NORMAL, "No, never heard of you.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = RoladDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ROLAD_1841)
}
