package content.region.asgarnia.dialogue.burthope

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Eadburg dialogue.
 */
@Initializable
class EadburgDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.FRIENDLY, "What's cooking, good looking?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.FRIENDLY, "The stew for the servant's main meal.").also { stage++ }
            1 -> playerl(FaceAnim.HALF_WORRIED, "Um...er...see you later.").also { stage++ }
            2 -> npcl(FaceAnim.FRIENDLY, "Bye!").also { stage++ }
            3 -> {
                end()
                openNpcShop(player, NPCs.ANTON_4295)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EADBURG_1072)
    }
}
