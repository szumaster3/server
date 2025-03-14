package content.region.asgarnia.dialogue.burthope

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Bernald dialogue.
 */
@Initializable
class BernaldDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.WORRIED, "Do you know anything about grapevine diseases?").also { stage++ }
            1 -> playerl(FaceAnim.FRIENDLY, "No, I'm afraid I don't.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.GUILTY,
                    "Oh, that's a shame. I hope I find someone soon, otherwise I could lose all of this year's crop.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BERNALD_2580)
    }
}
