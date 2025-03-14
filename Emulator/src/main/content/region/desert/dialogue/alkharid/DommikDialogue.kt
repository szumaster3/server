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
class DommikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HAPPY, "Would you like to buy some Crafting equipment?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "No, thanks, I've got all the Crafting equipment I need.",
                    "Let's see what you've got, then.",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "No, thanks, I've got all the Crafting equipment I need.",
                        ).also { stage++ }
                    2 -> {
                        end()
                        openNpcShop(player, NPCs.DOMMIK_545)
                    }
                }
            2 -> npcl(FaceAnim.FRIENDLY, "Okay. Fare well on your travels.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return DommikDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DOMMIK_545)
    }
}
