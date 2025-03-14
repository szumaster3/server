package content.region.misc.dialogue.keldagrim

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GulldamarDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Finest silver in all of Keldagrim, come and see!",
                ).also { stage++ }
            1 -> options("Right, what do you have?", "Not interested, thanks.").also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Right, what do you have?").also { stage++ }
                    2 -> playerl(FaceAnim.FRIENDLY, "Not interested, thanks.").also { stage = 5 }
                }
            3 -> npcl(FaceAnim.OLD_NORMAL, "Silver, what else!").also { stage++ }
            4 -> {
                end()
                openNpcShop(player, NPCs.GULLDAMAR_2159)
            }
            5 -> playerl(FaceAnim.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GulldamarDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GULLDAMAR_2159)
    }
}
