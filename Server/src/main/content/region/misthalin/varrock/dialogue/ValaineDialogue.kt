package content.region.misthalin.varrock.dialogue

import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Valaine dialogue.
 */
@Initializable
class ValaineDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Hello there. Want to have a look at what we're selling", "today?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("Yes please.", "No thank you.").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FaceAnim.HALF_GUILTY, "Yes please.").also { stage++ }
                2 -> playerl(FaceAnim.HALF_GUILTY, "No thank you.").also { stage = END_DIALOGUE }
            }
            2 -> {
                end()
                openNpcShop(player, NPCs.VALAINE_536)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = ValaineDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.VALAINE_536)
}
