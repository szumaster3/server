package content.region.asgarnia.taverley.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Gaius dialogue.
 */
@Initializable
class GaiusDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.FRIENDLY, "Welcome to my two-handed sword shop.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Let's trade.", "Thanks, but not today.").also { stage++ }
            1 -> when (buttonId) {
                1 -> end().also { openNpcShop(player, npc.id) }
                2 -> end()
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = GaiusDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.GAIUS_586)
}
