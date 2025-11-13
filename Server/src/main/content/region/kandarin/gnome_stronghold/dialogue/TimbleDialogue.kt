package content.region.kandarin.gnome_stronghold.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Timble dialogue.
 */
@Initializable
class TimbleDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_NORMAL, "Good day. How can I help you?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.FRIENDLY, "I was just wondering what you were up to.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_NORMAL, "I work for Aluft Aloft Gnome Food Delivery Service. It's a growing business you know. We get more customers every day!").also { stage++ }
            2 -> playerl(FaceAnim.HALF_ASKING, "So do you have any spare jobs then?").also { stage++ }
            3 -> npcl(FaceAnim.OLD_NORMAL, "Aluft Gianne jnr. is in charge - he'll be able to help you.").also { stage++ }
            4 -> playerl(FaceAnim.FRIENDLY, "Thanks.").also { stage++ }
            5 -> npcl(FaceAnim.OLD_NORMAL, "Have a nice day.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = TimbleDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.TIMBLE_4573)
}