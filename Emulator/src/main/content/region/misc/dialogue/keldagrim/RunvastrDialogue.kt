package content.region.misc.dialogue.keldagrim

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RunvastrDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.OLD_DEFAULT, "Oh, leave an old dwarf in peace will you?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "Why, what's wrong?").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Ninety years down in the mines, and this is all I got out of it. All my life wasted away.",
                ).also {
                    stage++
                }
            3 -> playerl(FaceAnim.FRIENDLY, "You've still got a nice house, don't you?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "A crummy old house in East, that's all my life amounts to.",
                ).also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "I'll leave you alone then.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RunvastrDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RUNVASTR_2190)
    }
}
