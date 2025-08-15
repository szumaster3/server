package content.region.other.zanaris.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Blaec dialogue.
 */
@Initializable
class BlaecDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..3).random()) {
            1 -> npc(FaceAnim.FRIENDLY, "Wunnerful weather we're having today!").also { stage = END_DIALOGUE }
            2 -> npc(FaceAnim.FRIENDLY, "Greetin's " + player.name + ", fine day today!").also { stage = END_DIALOGUE }
            3 ->
                npcl(FaceAnim.ANNOYED, "Please leave me alone, I'm busy trapping the pygmy shrews.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun newInstance(player: Player?): Dialogue = BlaecDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.BLAEC_3115)
}
