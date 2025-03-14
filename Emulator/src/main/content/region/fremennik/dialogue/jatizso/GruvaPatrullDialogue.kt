package content.region.fremennik.dialogue.jatizso

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GruvaPatrullDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Ho! Outerlander.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_ASKING, "What's down the scary-looking staircase?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "These are the stairs down to the mining caves. There are rich veins of many types down there, and miners too. Though be careful; some of the trolls occasionally sneak into the far end of the cave.",
                ).also {
                    stage++
                }
            2 -> playerl(FaceAnim.NEUTRAL, "Thanks. I'll look out for them.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GruvaPatrullDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRUVA_PATRULL_5500)
    }
}
