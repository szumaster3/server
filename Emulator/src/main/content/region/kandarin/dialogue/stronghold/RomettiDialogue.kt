package content.region.kandarin.dialogue.stronghold

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RomettiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Hello, traveller. Have a look at my latest range of gnome fashion. Rometti is the ultimate label in gnome high-society.",
                ).also {
                    stage++
                }

            1 -> playerl(FaceAnim.FRIENDLY, "Really.").also { stage++ }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Pastels are all the rage this season.").also { stage++ }
            3 -> options("Okay then, let's have a look.", "I've no time for fashion.").also { stage++ }
            4 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Okay then, let's have a look.").also { stage = 6 }
                    2 -> playerl(FaceAnim.NEUTRAL, "I've no time for fashion.").also { stage = 5 }
                }

            5 -> npcl(FaceAnim.OLD_NORMAL, "Hmm...I did wonder.").also { stage = END_DIALOGUE }
            6 -> end().also { openNpcShop(player, NPCs.ROMETTI_601) }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RomettiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROMETTI_601)
    }
}
