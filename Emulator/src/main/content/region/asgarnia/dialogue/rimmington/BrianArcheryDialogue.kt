package content.region.asgarnia.dialogue.rimmington

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BrianArcheryDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Would you like to buy some archery equipment?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "No thanks, I've got all the archery equipment I need.",
                    "Let's see what you've got, then.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "No, thanks, I've got all the archery equipment I need.",
                        ).also { stage++ }
                    2 -> end().also { openNpcShop(player, NPCs.BRIAN_1860) }
                }
            2 -> npc(FaceAnim.FRIENDLY, "Okay. Fare well on your travels.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRIAN_1860)
    }
}
