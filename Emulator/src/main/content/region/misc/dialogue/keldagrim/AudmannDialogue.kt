package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AudmannDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Oh, don't bother me human.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_ASKING, "Why not? What's wrong?").also { stage++ }
            1 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "You are wrong, human. Your attire is outrageous.",
                    "Your presence is obnoxious.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.HALF_ASKING, "What? What are you saying?").also { stage++ }
            3 -> npc(FaceAnim.OLD_NORMAL, "I'm saying you're in my way.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AudmannDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AUDMANN_2201)
    }
}
