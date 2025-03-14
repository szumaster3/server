package content.region.kandarin.handlers.guilds.wizards

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Professor Imblewyn dialogue.
 */
@Initializable
class ProfessorImblewynDialogue(
    player: Player? = null,
) : Dialogue(player) {
    /*
     * Info: Gnome wizard found in the Magic Guild.
     */

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player("I didn't realise gnomes were interested in magic.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Gnomes are interested in everything, lad.").also { stage++ }
            1 -> player("Of course.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PROFESSOR_IMBLEWYN_4586)
    }
}
