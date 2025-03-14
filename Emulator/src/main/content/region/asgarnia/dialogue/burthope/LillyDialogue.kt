package content.region.asgarnia.dialogue.burthope

import core.api.interaction.openNpcShop
import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Lilly dialogue.
 */
@Initializable
class LillyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_GUILTY, "Uh..... hi... didn't see you there. Can.... I help?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player(FaceAnim.HALF_GUILTY, "Umm... do you sell potions?").also { stage++ }
            1 -> npc(FaceAnim.HALF_GUILTY, "Erm... yes. When I'm not drinking them.").also { stage++ }
            2 ->
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "I'd like to see what you have for sale.",
                    "That's a pretty wall hanging.",
                    "Bye!",
                ).also {
                    stage++
                }
            3 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I'd like to see what you have for sale.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "That's a pretty wall hanging.").also { stage = 5 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Bye!").also { stage = 8 }
                }
            4 -> {
                end()
                openNpcShop(player, npc.id)
            }
            5 -> npc(FaceAnim.HALF_GUILTY, "Do you think so? I made it my self.").also { stage++ }
            6 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Really? Is that why there's all this cloth and dye around?",
                ).also { stage++ }
            7 -> npc(FaceAnim.HALF_GUILTY, "Yes, it's a hobby of mine when I'm.... relaxing.").also { stage++ }
            8 -> npc(FaceAnim.HALF_GUILTY, "Have fun and come back soon!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LILLY_4294)
    }
}
