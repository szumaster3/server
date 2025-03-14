package content.region.asgarnia.dialogue.rimmington

import core.api.interaction.openNpcShop
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RoomikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HAPPY, "Would you like to buy some Crafting equipment?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                sendDialogueOptions(
                    player,
                    "Choose an option:",
                    "Let's see what you've got, then.",
                    "No thanks.",
                ).also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> end().also { openNpcShop(player, NPCs.ROMMIK_585) }
                    2 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "No thanks, I've got all the crafting equipment I need.",
                        ).also { stage++ }
                }
            2 -> npc(FaceAnim.FRIENDLY, "Okay. Fare well on your travels.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROMMIK_585)
    }
}
