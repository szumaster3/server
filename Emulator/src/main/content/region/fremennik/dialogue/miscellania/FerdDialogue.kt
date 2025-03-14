package content.region.fremennik.dialogue.miscellania

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class FerdDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_DEFAULT, "Good day, sir.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.THINKING, "What are you doing down here?.").also { stage++ }
            1 -> npc(FaceAnim.OLD_DEFAULT, "Shoring up the walls.").also { stage++ }
            2 -> player(FaceAnim.ASKING, "What does that do?").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT, "Stops them falling down.").also { stage++ }
            4 -> player(FaceAnim.ASKING, "Oh, I see.").also { stage++ }
            5 ->
                npc(
                    FaceAnim.OLD_NOT_INTERESTED,
                    "Aye.",
                    "If you want to chatter, you'd better talk to ",
                    "Thorodin over there. I'm working.",
                ).also { stage++ }

            6 -> player(FaceAnim.ASKING, "Okay then.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return FerdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FERD_3937)
    }
}
