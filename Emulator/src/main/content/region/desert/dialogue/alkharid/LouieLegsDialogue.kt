package content.region.desert.dialogue.alkharid

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class LouieLegsDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Hey, wanna buy some armour?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What have you got?", "No, thank you.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.THINKING, "What have you got?").also { stage++ }
                    2 -> player(FaceAnim.FRIENDLY, "No, thank you.").also { stage = END_DIALOGUE }
                }

            2 -> npc(FaceAnim.HAPPY, "I provide items to help you keep your legs!").also { stage++ }
            3 -> {
                end()
                openNpcShop(player, NPCs.LOUIE_LEGS_542)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return LouieLegsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOUIE_LEGS_542)
    }
}
