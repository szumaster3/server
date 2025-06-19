package content.region.fremennik.island.jatizso.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MinaDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Ho! Outlander.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "What's down the scary-looking staircase?").also { stage++ }
            1 -> npcl(FaceAnim.NEUTRAL, "These are the stairs down to the mining caves.").also { stage++ }
            2 -> npcl(FaceAnim.NEUTRAL, "There are rich veins of many types down there, and miners too. Though be careful; some of the trolls occasionally sneak into the far end of the cave.").also { stage++ }
            3 -> playerl(FaceAnim.NEUTRAL, "Thanks. I'll look out for them.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MinaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GRUVA_PATRULL_5500)
}
