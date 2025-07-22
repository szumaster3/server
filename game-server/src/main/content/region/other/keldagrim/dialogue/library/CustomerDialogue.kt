package content.region.other.keldagrim.dialogue.library

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Customer (Keldagrim) dialogue.
 */
@Initializable
class CustomerDialogue(player: Player? = null) : Dialogue(player) {
    
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Oh, that's nice, another human visitor to Keldagrim!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "Indeed!").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "And what brings you to this city? Come to seek knowledge as well?",
                ).also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "I think it's mostly fame and riches I'm after.").also { stage++ }
            3 ->
                npcl(FaceAnim.FRIENDLY, "To each " + (if (player.isMale) "his" else "her") + " own, I suppose.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = CustomerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.CUSTOMER_2168)
}
