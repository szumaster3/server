package content.region.karamja.tzhaar.dialogue

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TzHaarDialogue : Dialogue {

    constructor()

    constructor(player: Player?) : super(player)

    override fun newInstance(player: Player): Dialogue {
        return TzHaarDialogue(player)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_FRIENDLY, "Can I help you JalYt-Ket-" + player.username + "?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("What do you have to trade?", "What did you call me?", "No I'm fine thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> end().also { openNpcShop(player, npc.id) }
                2 -> player("What did you call me?").also { stage++ }
                3 -> player("No I'm fine thanks.").also { stage = END_DIALOGUE }

            }

            2 -> npc(FaceAnim.CHILD_FRIENDLY, "Are you not JalYt-Ket?").also { stage++ }
            3 -> options("What's a 'JalYt-Ket'?", "I guess so...", "No I'm not!").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("What's a 'JalYt-Ket'?").also { stage++ }
                2 -> player("I guess so...").also { stage = 8 }
                3 -> player("No I'm not!").also { stage = END_DIALOGUE }
            }

            5 -> npc(FaceAnim.CHILD_FRIENDLY, "That what you are... you tough and strong no?").also { stage++ }
            6 -> player("Well yes I suppose I am...").also { stage++ }
            7 -> npc(FaceAnim.CHILD_FRIENDLY, "Then you JalYt-Ket!").also { stage = END_DIALOGUE }
            8 -> npc(FaceAnim.CHILD_FRIENDLY, "Well then, no problems.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TZHAAR_HUR_TEL_2620, NPCs.TZHAAR_HUR_LEK_2622, NPCs.TZHAAR_MEJ_ROH_2623)
    }
}