package content.region.fremennik.rellekka.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Olaf Hradson dialogue.
 */
@Initializable
class OlafHradsonDialogue(player: Player? = null, ) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.NEUTRAL, "Outlander, I have work to be getting on with... Please stop bothering me.").also { stage = END_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean = true

    override fun newInstance(player: Player?): Dialogue = OlafHradsonDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OLAF_HRADSON_2621)
}
