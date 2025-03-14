package content.region.misthalin.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AmbassadorFernookDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello Ambassador. Are you here visiting King Roald?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Well, in theory, but he always seems to be busy.").also { stage++ }
            1 -> player(FaceAnim.HALF_GUILTY, "You don't seem that upset by that, though...").also { stage++ }
            2 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Oh no, I like travelling, and if you become a diplomat",
                    "patience is a vital skill.",
                ).also {
                    stage++
                }
            3 -> player(FaceAnim.HALF_GUILTY, "I'll try to remember that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AMBASSADOR_FERRNOOK_4582)
    }
}
