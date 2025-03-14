package content.region.asgarnia.dialogue.burthope

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Helemos dialogue.
 */
@Initializable
class HelemosDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Greetings. Welcome to the Heroes' Guild.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("So do you sell anything here?", "So what can I do here?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "So do you sell anything good here?").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "So what can I do here?").also { stage = 4 }
                }
            2 -> npcl(FaceAnim.HAPPY, "Why yes! We DO run an exclusive shop for our members!").also { stage++ }
            3 -> {
                end()
                openNpcShop(player, npc.id)
            }
            4 ->
                npcl(
                    FaceAnim.HAPPY,
                    "Look around... there are all sorts of things to keep our guild members entertained!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HELEMOS_797)
    }
}
