package content.region.karamja.dialogue.apeatoll.bananaplantation

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BonzaraDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_DEFAULT, "It looks like you're trying to escape. Would you like some help?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes", "No").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.WORRIED, "I ... uh ... yes.").also { stage = 10 }
                    2 -> playerl(FaceAnim.ASKING, "No thank you. Who are you by the way?").also { stage = 20 }
                }
            10 -> npc(FaceAnim.OLD_DEFAULT, "Right you are.").also { stage++ }
            11 -> {
                end()
            }
            20 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Never mind that child. You should worry more about who you are and the nature of the forces that have driven you here.",
                ).also {
                    stage++
                }
            21 -> player(FaceAnim.THINKING, "I'll ... keep that in mind, thanks.").also { stage++ }
            22 -> npc(FaceAnim.OLD_DEFAULT, "We WILL meet again, " + player.name + ".").also { stage++ }
            23 -> player(FaceAnim.SUSPICIOUS, "Ok...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BonzaraDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BONZARA_1468)
    }
}
