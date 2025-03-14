package content.region.asgarnia.dialogue.burthope

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Lidio dialogue.
 */
@Initializable
class LidioDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.ASKING, "Greetings, warrior, how can I fill your stomach today?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.NEUTRAL, "With food preferable.").also { stage++ }
            1 -> {
                end()
                openNpcShop(player, npc.id)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LIDIO_4293)
    }
}
